package coding.challenge.api.error;

/**
 * Represents error information.
 *
 * @param code    Error code
 * @param message Error message
 */
public record Error(int code, String message) {
}
