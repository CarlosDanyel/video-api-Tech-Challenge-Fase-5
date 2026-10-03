package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application;
import com.fasterxml.jackson.databind.ObjectMapper;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.*;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.Video;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.User;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.OutboxEvent;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.Events;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.VideoStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ResultServiceTest {
    @Test void staleAttemptCannotChangeCurrentStatus() throws Exception {
        var videos = mock(VideoStore.class);
        var users = mock(UserStore.class);
        var outbox = mock(OutboxStore.class);
        var cache = mock(VideoCachePort.class);
        var mapper = new ObjectMapper();
        var video = Video.create(UUID.randomUUID(), "clip.mp4", "source");
        when(videos.findById(video.id())).thenReturn(Optional.of(video));
        var result = new Events.VideoResult(video.id(), UUID.randomUUID(), VideoStatus.FAILED, null, null, "old failure");
        new ResultService(videos, users, outbox, cache, mapper).apply(result);
        assertEquals(VideoStatus.QUEUED, video.status());
        verifyNoInteractions(users, outbox, cache);
    }
    @Test void failedAttemptCreatesOneNotification() throws Exception {
        var videos = mock(VideoStore.class);
        var users = mock(UserStore.class);
        var outbox = mock(OutboxStore.class);
        var cache = mock(VideoCachePort.class);
        var mapper = new ObjectMapper();
        var video = Video.create(UUID.randomUUID(), "clip.mp4", "source");
        when(videos.findById(video.id())).thenReturn(Optional.of(video));
        when(users.findById(video.userId())).thenReturn(Optional.of(User.create("a@example.com", "hash")));
        var result = new Events.VideoResult(video.id(), video.attemptId(), VideoStatus.FAILED, null, null, "bad file");
        var consumer = new ResultService(videos, users, outbox, cache, mapper);
        consumer.apply(result);
        consumer.apply(result);
        assertEquals(VideoStatus.FAILED, video.status());
        verify(outbox, times(1)).save(any(OutboxEvent.class));
    }
}
