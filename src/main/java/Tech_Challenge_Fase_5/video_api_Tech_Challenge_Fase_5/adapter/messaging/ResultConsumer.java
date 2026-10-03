package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.messaging;
import com.fasterxml.jackson.databind.ObjectMapper;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.config.RabbitConfig;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.ResultService;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.Events;
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
