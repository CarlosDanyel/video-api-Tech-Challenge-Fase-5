package com.fiapx.api.adapter.web;
import com.fiapx.api.application.VideoService;
import com.fiapx.api.application.VideoView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/videos")
@SecurityRequirement(name = "bearerAuth")
public class VideoController {
    private final VideoService videos;
    public VideoController(VideoService videos) { this.videos = videos; }
    @Operation(summary = "Upload a video for asynchronous processing")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoView> upload(@RequestPart("video") MultipartFile file) throws Exception {
        return ResponseEntity.accepted().body(videos.submit(userId(), file));
    }
    @GetMapping public List<VideoView> list() { return videos.list(userId()); }
    @GetMapping("/{id}") public VideoView get(@PathVariable UUID id) { return videos.get(userId(), id); }
    @GetMapping("/{id}/download") public ResponseEntity<InputStreamResource> download(@PathVariable UUID id) {
        var file = videos.download(userId(), id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
            .contentType(MediaType.parseMediaType("application/zip")).body(new InputStreamResource(file.stream()));
    }
    @PostMapping("/{id}/retry") public ResponseEntity<VideoView> retry(@PathVariable UUID id) {
        return ResponseEntity.accepted().body(videos.retry(userId(), id));
    }
    private UUID userId() { return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); }
}
