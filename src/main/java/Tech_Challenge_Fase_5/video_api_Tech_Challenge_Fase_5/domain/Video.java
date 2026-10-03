package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain;
import java.time.Instant;
import java.util.UUID;
public class Video {
    private final UUID id;
    private final UUID userId;
    private final String fileName;
    private final String sourceKey;
    private UUID attemptId;
    private VideoStatus status;
    private String outputKey;
    private Integer frameCount;
    private String errorMessage;
    private Instant createdAt;
    private Instant updatedAt;
    public Video(UUID id, UUID userId, String fileName, String sourceKey, UUID attemptId,
                 VideoStatus status, String outputKey, Integer frameCount, String errorMessage,
                 Instant createdAt, Instant updatedAt) {
        this.id = id; this.userId = userId; this.fileName = fileName; this.sourceKey = sourceKey;
        this.attemptId = attemptId; this.status = status; this.outputKey = outputKey;
        this.frameCount = frameCount; this.errorMessage = errorMessage;
        this.createdAt = createdAt; this.updatedAt = updatedAt;
    }
    public static Video create(UUID userId, String fileName, String sourceKey) {
        return new Video(UUID.randomUUID(), userId, fileName, sourceKey, UUID.randomUUID(),
            VideoStatus.QUEUED, null, null, null, null, null);
    }
    public void retry() {
        if (status != VideoStatus.FAILED) throw new IllegalStateException("Only failed videos can be retried");
        status = VideoStatus.QUEUED; attemptId = UUID.randomUUID(); errorMessage = null;
        outputKey = null; frameCount = null;
    }
    public void processing() { if (status == VideoStatus.QUEUED) status = VideoStatus.PROCESSING; }
    public void complete(String outputKey, int frameCount) {
        status = VideoStatus.COMPLETED; this.outputKey = outputKey; this.frameCount = frameCount;
        errorMessage = null;
    }
    public void fail(String errorMessage) {
        status = VideoStatus.FAILED; this.errorMessage = errorMessage;
    }
    public UUID id() { return id; }
    public UUID userId() { return userId; }
    public String fileName() { return fileName; }
    public String sourceKey() { return sourceKey; }
    public UUID attemptId() { return attemptId; }
    public VideoStatus status() { return status; }
    public String outputKey() { return outputKey; }
    public Integer frameCount() { return frameCount; }
    public String errorMessage() { return errorMessage; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }
}
