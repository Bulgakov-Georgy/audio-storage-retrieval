package coding.challenge.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User entity.
 */
@Entity
@Data
@NoArgsConstructor
public class User {

    /**
     * User identifier.
     */
    @Id
    @GeneratedValue
    private long id;

}
