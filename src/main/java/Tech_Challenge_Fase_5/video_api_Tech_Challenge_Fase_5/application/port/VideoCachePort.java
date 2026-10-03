package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.VideoView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface VideoCachePort {
    Optional<List<VideoView>> get(UUID userId);
    void put(UUID userId, List<VideoView> videos);
    void invalidate(UUID userId);
}
