package Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.adapter.web;
import Tech_Challenge_Fase_5.video_api_Tech_Challenge_Fase_5.application.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }
    public record Credentials(@Email @NotBlank String email, @NotBlank @Size(min = 8) String password) {}
    public record TokenResponse(String token, String tokenType) {}
    @PostMapping("/register") public ResponseEntity<TokenResponse> register(@Valid @RequestBody Credentials body) {
        return ResponseEntity.status(201).body(new TokenResponse(auth.register(body.email(), body.password()), "Bearer"));
    }
    @PostMapping("/login") public TokenResponse login(@Valid @RequestBody Credentials body) {
        return new TokenResponse(auth.login(body.email(), body.password()), "Bearer");
    }
}
