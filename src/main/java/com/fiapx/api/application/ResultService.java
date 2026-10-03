package com.fiapx.api.application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.api.application.port.*;
import com.fiapx.api.domain.*;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ResultService {
    private final VideoStore videos;
    private final UserStore users;
    private final OutboxStore outbox;
    private final VideoCachePort cache;
    private final ObjectMapper mapper;
    public ResultService(VideoStore videos, UserStore users, OutboxStore outbox,
                          VideoCachePort cache, ObjectMapper mapper) {
        this.videos = videos; this.users = users; this.outbox = outbox; this.cache = cache; this.mapper = mapper;
    }
    @Transactional
    public void apply(Events.VideoResult result) throws Exception {
        var video = videos.findById(result.videoId()).orElseThrow();
        if (!video.attemptId().equals(result.attemptId()) || video.status() == VideoStatus.COMPLETED || video.status() == VideoStatus.FAILED) return;
        if (result.status() == VideoStatus.PROCESSING) video.processing();
        if (result.status() == VideoStatus.COMPLETED) video.complete(result.outputKey(), result.frameCount());
        if (result.status() == VideoStatus.FAILED) {
            String error = result.errorMessage() == null ? "Processing failed" : result.errorMessage();
            video.fail(error.substring(0, Math.min(1000, error.length())));
            var user = users.findById(video.userId()).orElseThrow();
            var notification = new Events.FailureNotification(UUID.randomUUID(), video.id(), user.email(), video.errorMessage());
            outbox.save(OutboxEvent.create("video.failed", mapper.writeValueAsString(notification)));
        }
        videos.save(video);
        cache.invalidate(video.userId());
    }
}
