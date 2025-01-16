package coding.challenge.core.i18n;

import coding.challenge.core.context.ExecutionContext;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Contains a set of constants with identifiers of resource messages. Provides method to get a message from resource bundle.
 */
@UtilityClass
@Slf4j
public class Messages {

    public final String FAILED_TO_RETRIEVE_FILE = "failed_to_retrieve_file";

    public final String FAILED_TO_SAVE_FILE = "failed_to_save_file";

    public final String FILE_NOT_FOUND = "file_not_found";

    public final String INTERNAL_ERROR = "internal_error";

    public final String INVALID_ARGUMENT = "invalid_argument";

    public final String PHRASE_NOT_FOUND = "phrase_not_found";

    public final String UNSUPPORTED_AUDIO_TYPE = "unsupported_audio_type";

    public final String USER_NOT_FOUND = "user_not_found";

    private final Set<Locale> SUPPORTED_LOCALES = Set.of(Locale.ENGLISH);

    private final Map<Locale, ResourceBundle> RESOURCE_BUNDLES = new HashMap<>();

    private final ResourceBundle DEFAULT_RESOURCE_BUNDLE;

    static {
        for (Locale locale : SUPPORTED_LOCALES) {
            RESOURCE_BUNDLES.put(locale, ResourceBundle.getBundle("messages", locale));
        }
        DEFAULT_RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    }

    /**
     * Returns text of the message that is mapped to the key based on the locale. Can accept parameters to format the text using {@link String#format}
     *
     * @param key        The key of the message
     * @param parameters Parameters for the message
     * @return Text from resource bundle or empty string
     */
    public String getText(String key, Object... parameters) {
        Locale locale = Locale.forLanguageTag(ExecutionContext.get().getLanguage());
        ResourceBundle resourceBundle = RESOURCE_BUNDLES.get(locale);
        if (resourceBundle == null) {
            log.warn("Resource bundle for locale {} was not found", locale);
            return StringUtils.EMPTY;
        }
        String message;
        if (resourceBundle.containsKey(key)) {
            message = resourceBundle.getString(key);
        } else if (DEFAULT_RESOURCE_BUNDLE.containsKey(key)) {
            message = DEFAULT_RESOURCE_BUNDLE.getString(key);
        } else {
            log.warn("Resource string for key {} was not found", key);
            return StringUtils.EMPTY;
        }
        return String.format(message, parameters);
    }

}
