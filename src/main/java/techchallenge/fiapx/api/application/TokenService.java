package techchallenge.fiapx.api.application;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class TokenService {
    private final byte[] secret;
    private final ObjectMapper mapper;
    public TokenService(@Value("${security.jwt-secret}") String secret, ObjectMapper mapper) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        this.secret = secret.getBytes(StandardCharsets.UTF_8); this.mapper = mapper;
    }
    public String issue(UUID userId) {
        try {
            String header = encode(mapper.writeValueAsBytes(Map.of("alg", "HS256", "typ", "JWT")));
            String payload = encode(mapper.writeValueAsBytes(Map.of("sub", userId.toString(), "exp", Instant.now().plusSeconds(86400).getEpochSecond())));
            String body = header + "." + payload;
            return body + "." + encode(sign(body));
        } catch (Exception e) { throw new IllegalStateException("Could not issue token", e); }
    }
    public UUID verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException();
            String body = parts[0] + "." + parts[1];
            if (!java.security.MessageDigest.isEqual(sign(body), Base64.getUrlDecoder().decode(parts[2]))) throw new IllegalArgumentException();
            var header = mapper.readTree(Base64.getUrlDecoder().decode(parts[0]));
            var payload = mapper.readTree(Base64.getUrlDecoder().decode(parts[1]));
            if (!"HS256".equals(header.path("alg").asText()) || payload.path("exp").asLong() <= Instant.now().getEpochSecond()) throw new IllegalArgumentException();
            return UUID.fromString(payload.path("sub").asText());
        } catch (Exception e) { throw new IllegalArgumentException("Invalid token", e); }
    }
    private byte[] sign(String value) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }
    private String encode(byte[] value) { return Base64.getUrlEncoder().withoutPadding().encodeToString(value); }
}
