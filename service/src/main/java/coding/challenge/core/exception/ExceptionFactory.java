package coding.challenge.core.exception;

import coding.challenge.core.i18n.Messages;
import lombok.experimental.UtilityClass;

/**
 * Defines a set of convenience methods for creating common exceptions.
 */
@UtilityClass
public class ExceptionFactory {

    public ServiceException createInternalError(String messageKey, Throwable cause) {
        return new ServiceException(ErrorCodes.INTERNAL_ERROR, Messages.getText(messageKey), cause);
    }

    public ServiceException createObjectNotFoundException(String messageKey) {
        return new ServiceException(ErrorCodes.OBJECT_NOT_FOUND, Messages.getText(messageKey));
    }

    public ServiceException createUnsupportedAudioTypeException(String audioType) {
        return new ServiceException(
                ErrorCodes.UNSUPPORTED_AUDIO_TYPE,
                Messages.getText(Messages.UNSUPPORTED_AUDIO_TYPE, audioType)
        );
    }

}
