package coding.challenge.core.exception

/**
 * Defines an exception that is thrown by service methods.
 */
class ServiceException : RuntimeException {

    /**
     * The code of the error.
     */
    var errorCode: Int

    /**
     * Constructs a new service exception with the specified error code and detail message.
     *
     * @param errorCode The code of the error
     * @param message   The detail message
     */
    constructor(errorCode: Int, message: String?) : super(message) {
        this.errorCode = errorCode
    }

    /**
     * Constructs a new service exception with the specified error code, detail message and cause.
     *
     * @param errorCode The code of the error
     * @param message   The detail message
     * @param cause     The cause
     */
    constructor(errorCode: Int, message: String?, cause: Throwable) : super(message, cause) {
        this.errorCode = errorCode
    }

}
