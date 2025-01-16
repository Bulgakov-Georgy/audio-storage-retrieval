package coding.challenge.core.repository;

import coding.challenge.core.model.UserPhrase;
import org.springframework.data.repository.CrudRepository;

/**
 * Represents repository for management of {@link UserPhrase} entity.
 */
public interface UserPhraseRepository extends CrudRepository<UserPhrase, UserPhrase.UserPhraseId> {
}
