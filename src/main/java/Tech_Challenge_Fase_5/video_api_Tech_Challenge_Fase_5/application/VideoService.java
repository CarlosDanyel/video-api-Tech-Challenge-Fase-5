package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.*;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.*;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
@Service
public class VideoService {
    private final VideoStore videos;
    private final OutboxStore outbox;
    private final VideoStoragePort storage;
    private final VideoCachePort cache;
    private final ObjectMapper mapper;
    private final TransactionTemplate transactions;
    public VideoService(VideoStore videos, OutboxStore outbox, VideoStoragePort storage,
                        VideoCachePort cache, ObjectMapper mapper, TransactionTemplate transactions) {
        this.videos = videos; this.outbox = outbox; this.storage = storage;
        this.cache = cache; this.mapper = mapper; this.transactions = transactions;
    }
    public VideoView submit(UUID userId, MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new IllegalArgumentException("Video is empty");
        String name = file.getOriginalFilename() == null ? "video" : file.getOriginalFilename().replace('\\', '/');
        name = name.substring(name.lastIndexOf('/') + 1);
        if (!name.toLowerCase().matches(".*\\.(mp4|avi|mov|mkv)")) throw new IllegalArgumentException("Unsupported video format");
        if (name.length() > 255) throw new IllegalArgumentException("File name is too long");
        String key = "uploads/" + UUID.randomUUID() + "/source";
        try (InputStream input = file.getInputStream()) {
            storage.put(key, input, file.getSize(), file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        }
        try {
            String fileName = name;
            VideoView view = transactions.execute(status -> {
                var video = videos.save(Video.create(userId, fileName, key));
                outbox.save(OutboxEvent.create("video.requested", json(new Events.VideoRequested(video.id(), video.attemptId(), key))));
                return VideoView.from(video);
            });
            cache.invalidate(userId);
            return view;
        } catch (RuntimeException e) {
            storage.delete(key);
            throw e;
        }
    }
    public List<VideoView> list(UUID userId) {
        return cache.get(userId).orElseGet(() -> {
            List<VideoView> result = videos.findByUserIdOrderByCreatedAtDesc(userId).stream().map(VideoView::from).toList();
            cache.put(userId, result);
            return result;
        });
    }
    public VideoView get(UUID userId, UUID id) { return VideoView.from(owned(userId, id)); }
    public Download download(UUID userId, UUID id) {
        var video = owned(userId, id);
        if (video.status() != VideoStatus.COMPLETED || video.outputKey() == null) throw new ResponseStatusException(HttpStatus.CONFLICT, "Video is not ready");
        return new Download("frames_" + id + ".zip", storage.get(video.outputKey()));
    }
    public VideoView retry(UUID userId, UUID id) {
        VideoView view = transactions.execute(status -> {
            var video = owned(userId, id);
            if (video.status() != VideoStatus.FAILED) throw new ResponseStatusException(HttpStatus.CONFLICT, "Only failed videos can be retried");
            video.retry();
            var updated = videos.save(video);
            outbox.save(OutboxEvent.create("video.requested", json(new Events.VideoRequested(updated.id(), updated.attemptId(), updated.sourceKey()))));
            return VideoView.from(updated);
        });
        cache.invalidate(userId);
        return view;
    }
    private Video owned(UUID userId, UUID id) {
        return videos.findByIdAndUserId(id, userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Video not found"));
    }
    private String json(Object value) {
        try { return mapper.writeValueAsString(value); }
        catch (JsonProcessingException e) { throw new IllegalStateException(e); }
    }
    public record Download(String fileName, InputStream stream) {}
}
