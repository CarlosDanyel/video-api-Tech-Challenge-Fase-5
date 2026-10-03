package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.OutboxEvent;
import java.util.List;
import java.util.UUID;
public interface OutboxStore {
    void save(OutboxEvent event);
    List<OutboxEvent> findPending();
    void markPublished(UUID id);
}
