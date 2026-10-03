package techchallenge.fiapx.api.adapter.messaging;
import com.fasterxml.jackson.databind.ObjectMapper;
import techchallenge.fiapx.api.adapter.config.RabbitConfig;
import techchallenge.fiapx.api.application.ResultService;
import techchallenge.fiapx.api.domain.Events;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class ResultConsumer {
    private final ResultService results;
    private final ObjectMapper mapper;
    public ResultConsumer(ResultService results, ObjectMapper mapper) { this.results = results; this.mapper = mapper; }
    @RabbitListener(queues = RabbitConfig.RESULT_QUEUE)
    public void receive(String payload) throws Exception {
        results.apply(mapper.readValue(payload, Events.VideoResult.class));
    }
}
