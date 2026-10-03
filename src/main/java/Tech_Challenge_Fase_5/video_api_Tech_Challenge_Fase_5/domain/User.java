package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain;
import java.time.Instant;
import java.util.UUID;
public record User(UUID id, String email, String passwordHash, Instant createdAt, Instant updatedAt) {
    public static User create(String email, String passwordHash) {
        return new User(UUID.randomUUID(), email, passwordHash, null, null);
    }
}
