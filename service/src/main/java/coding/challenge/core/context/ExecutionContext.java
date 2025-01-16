package coding.challenge.core.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

/**
 * Contains information about execution context fo the current request.
 */
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutionContext {

    private String language;

    private static final ThreadLocal<ExecutionContext> CONTEXT = ThreadLocal.withInitial(ExecutionContext::new);

    public static ExecutionContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * Returns language code specified in the request or default locale language.
     *
     * @return Language code
     */
    public String getLanguage() {
        return language == null ? Locale.getDefault().getLanguage() : language;
    }

}
