package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.persistence;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.VideoStore;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.Video;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
@Component
public class JpaVideoStore implements VideoStore {
    private final VideoRepository repository;
    public JpaVideoStore(VideoRepository repository) { this.repository = repository; }
    public Video save(Video video) {
        var entity = repository.findById(video.id()).orElseGet(() -> {
            var created = new VideoEntity(video.userId(), video.fileName(), video.sourceKey());
            created.id = video.id();
            return created;
        });
        entity.attemptId = video.attemptId();
        entity.status = video.status();
        entity.outputKey = video.outputKey();
        entity.frameCount = video.frameCount();
        entity.errorMessage = video.errorMessage();
        return map(repository.saveAndFlush(entity));
    }
    public Optional<Video> findById(UUID id) { return repository.findById(id).map(this::map); }
    public Optional<Video> findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).map(this::map);
    }
    public List<Video> findByUserIdOrderByCreatedAtDesc(UUID userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::map).toList();
    }
    private Video map(VideoEntity entity) {
        return new Video(entity.id, entity.userId, entity.fileName, entity.sourceKey,
            entity.attemptId, entity.status, entity.outputKey, entity.frameCount,
            entity.errorMessage, entity.createdAt, entity.updatedAt);
    }
}
