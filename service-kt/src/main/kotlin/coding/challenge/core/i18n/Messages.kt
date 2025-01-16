package coding.challenge.core.i18n

import coding.challenge.core.context.ExecutionContext
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import java.util.Locale
import java.util.ResourceBundle

/**
 * Contains a set of constants with identifiers of resource messages. Provides method to get a message from resource bundle.
 */
object Messages {

    const val FAILED_TO_RETRIEVE_FILE: String = "failed_to_retrieve_file"

    const val FAILED_TO_SAVE_FILE: String = "failed_to_save_file"

    const val FILE_NOT_FOUND: String = "file_not_found"

    const val INTERNAL_ERROR: String = "internal_error"

    const val INVALID_ARGUMENT: String = "invalid_argument"

    const val PHRASE_NOT_FOUND: String = "phrase_not_found"

    const val UNSUPPORTED_AUDIO_TYPE: String = "unsupported_audio_type"

    const val USER_NOT_FOUND: String = "user_not_found"

    private val log = LoggerFactory.getLogger(this::class.java)

    private val SUPPORTED_LOCALES: Set<Locale> = setOf(Locale.ENGLISH)

    private val RESOURCE_BUNDLES: MutableMap<Locale, ResourceBundle> = mutableMapOf()

    private val DEFAULT_RESOURCE_BUNDLE: ResourceBundle

    init {
        for (locale in SUPPORTED_LOCALES) {
            RESOURCE_BUNDLES[locale] = ResourceBundle.getBundle("messages", locale)
        }
        DEFAULT_RESOURCE_BUNDLE = ResourceBundle.getBundle("messages")
    }

    /**
     * Returns text of the message that is mapped to the key based on the locale. Can accept parameters to format the text using [String.format]
     *
     * @param key        The key of the message
     * @param parameters Parameters for the message
     * @return Text from resource bundle or empty string
     */
    fun getText(key: String, vararg parameters: Any?): String {
        val locale = Locale.forLanguageTag(ExecutionContext.get().language)
        val resourceBundle = RESOURCE_BUNDLES[locale]
        if (resourceBundle == null) {
            log.warn("Resource bundle for locale {} was not found", locale)
            return StringUtils.EMPTY
        }
        val message =
            if (resourceBundle.containsKey(key)) {
                resourceBundle.getString(key)
            } else if (DEFAULT_RESOURCE_BUNDLE.containsKey(key)) {
                DEFAULT_RESOURCE_BUNDLE.getString(key)
            } else {
                log.warn("Resource string for key {} was not found", key)
                return StringUtils.EMPTY
            }
        return String.format(message, *parameters)
    }

}
