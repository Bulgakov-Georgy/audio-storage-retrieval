package coding.challenge.api.error

import coding.challenge.core.exception.ErrorCodes
import coding.challenge.core.exception.ServiceException
import coding.challenge.core.i18n.Messages
import coding.challenge.core.model.AudioFileFormat
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException

/**
 * Handles exceptions and converts them into HTTP response with [Error].
 */
@RestControllerAdvice
class ApiExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<Error> {
        log.error("Failed to process request", exception)
        return ResponseEntity.status(getHttpStatus(exception)).body(getError(exception))
    }

}

private fun getHttpStatus(exception: Exception): HttpStatus =
    when (exception) {
        is MissingServletRequestPartException,
        is MethodArgumentTypeMismatchException,
            -> {
            HttpStatus.BAD_REQUEST
        }

        is ServiceException -> {
            when (exception.errorCode) {
                ErrorCodes.INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST

                ErrorCodes.OBJECT_NOT_FOUND -> HttpStatus.NOT_FOUND

                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }
        }

        else -> {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

private fun getError(exception: Exception): Error =
    when (exception) {
        is MissingServletRequestPartException -> Error(
            ErrorCodes.INVALID_ARGUMENT,
            Messages.getText(Messages.INVALID_ARGUMENT, exception.requestPartName)
        )

        is MethodArgumentTypeMismatchException -> {
            if (AudioFileFormat::class.java == exception.requiredType) {
                Error(
                    ErrorCodes.UNSUPPORTED_AUDIO_TYPE,
                    Messages.getText(Messages.UNSUPPORTED_AUDIO_TYPE, exception.value?.toString())
                )
            }
            Error(
                ErrorCodes.INVALID_ARGUMENT,
                Messages.getText(Messages.INVALID_ARGUMENT, exception.name)
            )
        }

        is ServiceException -> {
            Error(exception.errorCode, exception.message)
        }

        else -> {
            Error(ErrorCodes.INTERNAL_ERROR, Messages.getText(Messages.INTERNAL_ERROR))
        }
    }
