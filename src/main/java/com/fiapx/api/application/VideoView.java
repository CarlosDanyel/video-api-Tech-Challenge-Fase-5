package com.fiapx.api.application;
import com.fiapx.api.domain.Video;
import com.fiapx.api.domain.VideoStatus;
import java.time.Instant;
import java.util.UUID;
public record VideoView(UUID id, String fileName, VideoStatus status, Integer frameCount,
                        String errorMessage, Instant createdAt, Instant updatedAt) {
    public static VideoView from(Video video) {
        return new VideoView(video.id(), video.fileName(), video.status(), video.frameCount(),
            video.errorMessage(), video.createdAt(), video.updatedAt());
    }
}
