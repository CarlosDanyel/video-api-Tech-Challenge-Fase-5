package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.messaging;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.config.RabbitConfig;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.OutboxStore;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class OutboxRelay {
    private final OutboxStore outbox;
    private final RabbitTemplate rabbit;
    public OutboxRelay(OutboxStore outbox, RabbitTemplate rabbit) {
        this.outbox = outbox; this.rabbit = rabbit; this.rabbit.setMandatory(true);
    }
    @Scheduled(fixedDelayString = "${outbox.delay-ms:1000}")
    @Transactional
    public void publish() throws Exception {
        for (var event : outbox.findPending()) {
            var confirmation = new CorrelationData(event.id().toString());
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, event.routingKey(), event.payload(), message -> {
                message.getMessageProperties().setDeliveryMode(org.springframework.amqp.core.MessageDeliveryMode.PERSISTENT);
                message.getMessageProperties().setMessageId(event.id().toString());
                return message;
            }, confirmation);
            var result = confirmation.getFuture().get(10, TimeUnit.SECONDS);
            if (!result.isAck() || confirmation.getReturned() != null) throw new IllegalStateException("RabbitMQ did not route event " + event.id());
            outbox.markPublished(event.id());
        }
    }
}
