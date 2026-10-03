package techchallenge.fiapx.api.adapter.persistence;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "users")
public class UserEntity {
    @Id public UUID id;
    @Column(nullable = false, unique = true) public String email;
    @Column(name = "password_hash", nullable = false) public String passwordHash;
    @Column(name = "created_at", nullable = false) public Instant createdAt;
    @Column(name = "updated_at", nullable = false) public Instant updatedAt;
    protected UserEntity() {}
    public UserEntity(String email, String passwordHash) {
        this.id = UUID.randomUUID(); this.email = email; this.passwordHash = passwordHash;
    }
    @PrePersist void create() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
}
