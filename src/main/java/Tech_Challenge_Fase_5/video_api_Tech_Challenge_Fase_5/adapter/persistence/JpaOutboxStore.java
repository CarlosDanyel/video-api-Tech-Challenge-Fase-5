package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.persistence;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.OutboxStore;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.OutboxEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
@Component
public class JpaOutboxStore implements OutboxStore {
    private final OutboxRepository repository;
    public JpaOutboxStore(OutboxRepository repository) { this.repository = repository; }
    public void save(OutboxEvent event) {
        var entity = new OutboxEntity(event.routingKey(), event.payload());
        entity.id = event.id();
        repository.save(entity);
    }
    public List<OutboxEvent> findPending() {
        return repository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc().stream()
            .map(e -> new OutboxEvent(e.id, e.routingKey, e.payload, e.createdAt, e.updatedAt, e.publishedAt)).toList();
    }
    public void markPublished(UUID id) {
        var entity = repository.findById(id).orElseThrow();
        entity.publishedAt = Instant.now();
        repository.save(entity);
    }
}
