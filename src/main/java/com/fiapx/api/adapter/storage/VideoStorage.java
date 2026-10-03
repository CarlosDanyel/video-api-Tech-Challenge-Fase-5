package com.fiapx.api.adapter.storage;
import java.io.InputStream;
import com.fiapx.api.application.port.VideoStoragePort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
@Component
public class VideoStorage implements VideoStoragePort {
    private final S3Client s3;
    private final String bucket;
    public VideoStorage(S3Client s3, @Value("${storage.bucket}") String bucket) {
        this.s3 = s3; this.bucket = bucket;
    }
    @PostConstruct
    public void ensureBucket() {
        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception missing) {
            if (missing.statusCode() != 404) throw missing;
            try {
                s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            } catch (S3Exception conflict) {
                if (conflict.statusCode() != 409) throw conflict;
                s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            }
        }
    }
    public void put(String key, InputStream input, long size, String contentType) {
        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build(),
            RequestBody.fromInputStream(input, size));
    }
    public InputStream get(String key) {
        return s3.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
    }
    public void delete(String key) { s3.deleteObject(b -> b.bucket(bucket).key(key)); }
}
