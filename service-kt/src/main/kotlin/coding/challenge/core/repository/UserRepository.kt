package coding.challenge.core.repository

import coding.challenge.core.model.User
import org.springframework.data.repository.CrudRepository

/**
 * Represents repository for management of [User] entity.
 */
interface UserRepository : CrudRepository<User, Long>
