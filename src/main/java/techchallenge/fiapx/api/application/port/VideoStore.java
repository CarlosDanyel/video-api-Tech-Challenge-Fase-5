package techchallenge.fiapx.api.application.port;
import techchallenge.fiapx.api.domain.Video;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface VideoStore {
    Video save(Video video);
    Optional<Video> findById(UUID id);
    Optional<Video> findByIdAndUserId(UUID id, UUID userId);
    List<Video> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
