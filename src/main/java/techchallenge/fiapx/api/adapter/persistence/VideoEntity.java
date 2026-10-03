package techchallenge.fiapx.api.adapter.persistence;
import techchallenge.fiapx.api.domain.VideoStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "videos")
public class VideoEntity {
    @Id public UUID id;
    @Version public long version;
    @Column(name = "user_id", nullable = false) public UUID userId;
    @Column(name = "file_name", nullable = false) public String fileName;
    @Column(name = "source_key", nullable = false) public String sourceKey;
    @Column(name = "attempt_id", nullable = false) public UUID attemptId;
    @Column(name = "output_key") public String outputKey;
    @Enumerated(EnumType.STRING) @Column(nullable = false) public VideoStatus status;
    @Column(name = "frame_count") public Integer frameCount;
    @Column(name = "error_message") public String errorMessage;
    @Column(name = "created_at", nullable = false) public Instant createdAt;
    @Column(name = "updated_at", nullable = false) public Instant updatedAt;
    protected VideoEntity() {}
    public VideoEntity(UUID userId, String fileName, String sourceKey) {
        id = UUID.randomUUID(); this.userId = userId; this.fileName = fileName;
        this.sourceKey = sourceKey; attemptId = UUID.randomUUID(); status = VideoStatus.QUEUED;
    }
    @PrePersist void create() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void update() { updatedAt = Instant.now(); }
}
