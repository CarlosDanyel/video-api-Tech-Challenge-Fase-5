package com.fiapx.api.application.port;
import com.fiapx.api.domain.User;
import java.util.Optional;
import java.util.UUID;
public interface UserStore {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    User save(User user);
}
