package com.fiapx.api.application.port;
import com.fiapx.api.application.VideoView;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface VideoCachePort {
    Optional<List<VideoView>> get(UUID userId);
    void put(UUID userId, List<VideoView> videos);
    void invalidate(UUID userId);
}
