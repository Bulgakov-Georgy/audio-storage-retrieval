package coding.challenge.api.error

/**
 * Represents error information.
 *
 * @param code    Error code
 * @param message Error message
 */
data class Error(val code: Int, val message: String?)
