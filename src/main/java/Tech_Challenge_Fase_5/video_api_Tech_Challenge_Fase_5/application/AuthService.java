package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.port.UserStore;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.domain.User;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthService {
    private final UserStore users;
    private final PasswordEncoder passwords;
    private final TokenService tokens;
    public AuthService(UserStore users, PasswordEncoder passwords, TokenService tokens) {
        this.users = users; this.passwords = passwords; this.tokens = tokens;
    }
    @Transactional
    public String register(String email, String password) {
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmail(normalized)) throw new IllegalArgumentException("Email already registered");
        var user = users.save(User.create(normalized, passwords.encode(password)));
        return tokens.issue(user.id());
    }
    public String login(String email, String password) {
        var user = users.findByEmail(email.trim().toLowerCase(Locale.ROOT))
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwords.matches(password, user.passwordHash())) throw new IllegalArgumentException("Invalid credentials");
        return tokens.issue(user.id());
    }
}
