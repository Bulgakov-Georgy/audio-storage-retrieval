package coding.challenge.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Phrase entity.
 */
@Entity
@Data
@NoArgsConstructor
public class Phrase {

    /**
     * Phrase identifier.
     */
    @Id
    @GeneratedValue
    private long id;

}
