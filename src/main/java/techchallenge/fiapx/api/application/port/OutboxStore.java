package techchallenge.fiapx.api.application.port;
import techchallenge.fiapx.api.domain.OutboxEvent;
import java.util.List;
import java.util.UUID;
public interface OutboxStore {
    void save(OutboxEvent event);
    List<OutboxEvent> findPending();
    void markPublished(UUID id);
}
