package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain;
import java.util.UUID;
public final class Events {
    private Events() {}
    public record VideoRequested(UUID videoId, UUID attemptId, String sourceKey) {}
    public record VideoResult(UUID videoId, UUID attemptId, VideoStatus status, String outputKey, Integer frameCount, String errorMessage) {}
    public record FailureNotification(UUID eventId, UUID videoId, String email, String reason) {}
}
