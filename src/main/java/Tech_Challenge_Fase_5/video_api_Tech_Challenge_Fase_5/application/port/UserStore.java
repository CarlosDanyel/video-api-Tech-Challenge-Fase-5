package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.User;
import java.util.Optional;
import java.util.UUID;
public interface UserStore {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    User save(User user);
}
