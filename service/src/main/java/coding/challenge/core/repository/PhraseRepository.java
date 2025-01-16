package coding.challenge.core.repository;

import coding.challenge.core.model.Phrase;
import org.springframework.data.repository.CrudRepository;

/**
 * Represents repository for management of {@link Phrase} entity.
 */
public interface PhraseRepository extends CrudRepository<Phrase, Long> {
}
