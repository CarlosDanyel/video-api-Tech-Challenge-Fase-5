package techchallenge.fiapx.api.domain;
import java.time.Instant;
import java.util.UUID;
public record OutboxEvent(UUID id, String routingKey, String payload, Instant createdAt,
                          Instant updatedAt, Instant publishedAt) {
    public static OutboxEvent create(String routingKey, String payload) {
        return new OutboxEvent(UUID.randomUUID(), routingKey, payload, null, null, null);
    }
}
