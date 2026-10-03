package techchallenge.fiapx.api.adapter.persistence;
import techchallenge.fiapx.api.application.port.UserStore;
import techchallenge.fiapx.api.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
@Component
public class JpaUserStore implements UserStore {
    private final UserRepository repository;
    public JpaUserStore(UserRepository repository) { this.repository = repository; }
    public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
    public Optional<User> findByEmail(String email) { return repository.findByEmail(email).map(this::map); }
    public Optional<User> findById(UUID id) { return repository.findById(id).map(this::map); }
    public User save(User user) {
        var entity = new UserEntity(user.email(), user.passwordHash());
        entity.id = user.id();
        return map(repository.saveAndFlush(entity));
    }
    private User map(UserEntity entity) {
        return new User(entity.id, entity.email, entity.passwordHash, entity.createdAt, entity.updatedAt);
    }
}
