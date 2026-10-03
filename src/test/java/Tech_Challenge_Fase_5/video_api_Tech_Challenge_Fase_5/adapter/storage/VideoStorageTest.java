package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.storage;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import static org.mockito.Mockito.*;
class VideoStorageTest {
    @Test void missingBucketIsCreated() {
        var s3 = mock(S3Client.class);
        when(s3.headBucket(any(HeadBucketRequest.class)))
            .thenThrow(S3Exception.builder().statusCode(404).message("missing").build());
        new VideoStorage(s3, "videos").ensureBucket();
        verify(s3).createBucket(any(CreateBucketRequest.class));
    }
}
