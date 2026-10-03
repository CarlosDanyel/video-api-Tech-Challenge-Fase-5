package techchallenge.fiapx.api.adapter.persistence;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "outbox_events")
public class OutboxEntity {
    @Id public UUID id;
    @Column(nullable = false) public String routingKey;
    @Column(nullable = false, columnDefinition = "text") public String payload;
    @Column(name = "published_at") public Instant publishedAt;
    @Column(name = "created_at", nullable = false) public Instant createdAt;
    @Column(name = "updated_at", nullable = false) public Instant updatedAt;
    protected OutboxEntity() {}
    public OutboxEntity(String routingKey, String payload) {
        id = UUID.randomUUID(); this.routingKey = routingKey; this.payload = payload;
    }
    @PrePersist void create() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
}
