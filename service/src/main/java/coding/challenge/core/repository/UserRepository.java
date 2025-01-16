package coding.challenge.core.repository;

import coding.challenge.core.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Represents repository for management of {@link User} entity.
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
