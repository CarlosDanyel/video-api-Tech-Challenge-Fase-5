package techchallenge.fiapx.api.adapter.cache;
import techchallenge.fiapx.api.application.VideoView;
import techchallenge.fiapx.api.application.port.VideoCachePort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
@Component
public class RedisVideoCache implements VideoCachePort {
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper;
    public RedisVideoCache(StringRedisTemplate redis, ObjectMapper mapper) { this.redis = redis; this.mapper = mapper; }
    public Optional<List<VideoView>> get(UUID userId) {
        try {
            String value = redis.opsForValue().get(key(userId));
            return value == null ? Optional.empty() : Optional.of(mapper.readValue(value, new TypeReference<>() {}));
        } catch (Exception e) { return Optional.empty(); }
    }
    public void put(UUID userId, List<VideoView> videos) {
        try { redis.opsForValue().set(key(userId), mapper.writeValueAsString(videos), Duration.ofSeconds(30)); }
        catch (Exception ignored) { }
    }
    public void invalidate(UUID userId) {
        try { redis.delete(key(userId)); } catch (Exception ignored) { }
    }
    private String key(UUID userId) { return "videos:" + userId; }
}
