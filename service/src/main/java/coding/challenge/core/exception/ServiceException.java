package coding.challenge.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Defines an exception that is thrown by service methods.
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {

    /**
     * The code of the error.
     */
    int errorCode;

    /**
     * Constructs a new service exception with the specified error code and detail message.
     *
     * @param errorCode The code of the error
     * @param message   The detail message
     */
    public ServiceException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new service exception with the specified error code, detail message and cause.
     *
     * @param errorCode The code of the error
     * @param message   The detail message
     * @param cause     The cause
     */
    public ServiceException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
