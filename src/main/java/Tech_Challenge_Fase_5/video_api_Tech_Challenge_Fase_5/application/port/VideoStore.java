package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.Video;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface VideoStore {
    Video save(Video video);
    Optional<Video> findById(UUID id);
    Optional<Video> findByIdAndUserId(UUID id, UUID userId);
    List<Video> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
