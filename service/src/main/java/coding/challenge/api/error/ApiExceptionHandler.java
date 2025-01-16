package coding.challenge.api.error;

import coding.challenge.core.exception.ErrorCodes;
import coding.challenge.core.exception.ServiceException;
import coding.challenge.core.i18n.Messages;
import coding.challenge.core.model.AudioFileFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * Handles exceptions and converts them into HTTP response with {@link Error}.
 */
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Error> handle(Exception exception) {
        log.error("Failed to process request", exception);
        return ResponseEntity.status(getHttpStatus(exception)).body(getError(exception));
    }

    private static HttpStatus getHttpStatus(Exception exception) {
        if (exception instanceof MissingServletRequestPartException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof MethodArgumentTypeMismatchException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof ServiceException serviceException) {
            switch (serviceException.getErrorCode()) {
                case ErrorCodes.INVALID_ARGUMENT -> {
                    return HttpStatus.BAD_REQUEST;
                }
                case ErrorCodes.OBJECT_NOT_FOUND -> {
                    return HttpStatus.NOT_FOUND;
                }
                default -> {
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private static Error getError(Exception exception) {
        if (exception instanceof MissingServletRequestPartException ex) {
            return new Error(
                    ErrorCodes.INVALID_ARGUMENT,
                    Messages.getText(Messages.INVALID_ARGUMENT, ex.getRequestPartName())
            );
        }
        if (exception instanceof MethodArgumentTypeMismatchException argumentException) {
            if (AudioFileFormat.class.equals(argumentException.getRequiredType())) {
                return new Error(
                        ErrorCodes.UNSUPPORTED_AUDIO_TYPE,
                        Messages.getText(Messages.UNSUPPORTED_AUDIO_TYPE, String.valueOf(argumentException.getValue()))
                );
            }
            return new Error(
                    ErrorCodes.INVALID_ARGUMENT,
                    Messages.getText(Messages.INVALID_ARGUMENT, argumentException.getName())
            );
        }
        if (exception instanceof ServiceException serviceException) {
            return new Error(serviceException.getErrorCode(), serviceException.getMessage());
        }
        return new Error(ErrorCodes.INTERNAL_ERROR, Messages.getText(Messages.INTERNAL_ERROR));
    }

}
