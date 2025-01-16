package coding.challenge.core.exception

import coding.challenge.core.i18n.Messages

/**
 * Defines a set of convenience methods for creating common exceptions.
 */
object ExceptionFactory {

    fun createInternalError(messageKey: String, cause: Throwable): ServiceException =
        ServiceException(ErrorCodes.INTERNAL_ERROR, Messages.getText(messageKey), cause)

    fun createObjectNotFoundException(messageKey: String): ServiceException =
        ServiceException(ErrorCodes.OBJECT_NOT_FOUND, Messages.getText(messageKey))

    fun createUnsupportedAudioTypeException(audioType: String): ServiceException =
        ServiceException(
            ErrorCodes.UNSUPPORTED_AUDIO_TYPE,
            Messages.getText(Messages.UNSUPPORTED_AUDIO_TYPE, audioType)
        )

}
