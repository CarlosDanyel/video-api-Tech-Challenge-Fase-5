package techchallenge.fiapx.api.adapter.persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {
    List<VideoEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<VideoEntity> findByIdAndUserId(UUID id, UUID userId);
}
