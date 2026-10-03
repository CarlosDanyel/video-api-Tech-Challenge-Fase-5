package com.fiapx.api.application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.api.application.port.*;
import com.fiapx.api.domain.Video;

import com.fiapx.api.domain.VideoStatus;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class VideoServiceTest {
    private final VideoStore videos = mock(VideoStore.class);
    private final OutboxStore outbox = mock(OutboxStore.class);
    private final VideoStoragePort storage = mock(VideoStoragePort.class);
    private final VideoCachePort cache = mock(VideoCachePort.class);
    private final TransactionTemplate transactions = mock(TransactionTemplate.class);
    private final VideoService service = new VideoService(videos, outbox, storage, cache, new ObjectMapper(), transactions);
    @Test void anotherUsersVideoCannotBeDownloaded() {
        UUID user = UUID.randomUUID(), video = UUID.randomUUID();
        when(videos.findByIdAndUserId(video, user)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.download(user, video));
        verifyNoInteractions(storage);
    }
    @Test void unfinishedVideoCannotBeDownloaded() {
        UUID user = UUID.randomUUID();
        var video = Video.create(user, "movie.mp4", "uploads/source");
        when(videos.findByIdAndUserId(video.id(), user)).thenReturn(Optional.of(video));
        assertThrows(ResponseStatusException.class, () -> service.download(user, video.id()));
        verifyNoInteractions(storage);
    }
    @Test void unsupportedUploadNeverReachesStorage() {
        var upload = new MockMultipartFile("video", "notes.txt", "text/plain", "data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.submit(UUID.randomUUID(), upload));
        verifyNoInteractions(storage);
    }
}
