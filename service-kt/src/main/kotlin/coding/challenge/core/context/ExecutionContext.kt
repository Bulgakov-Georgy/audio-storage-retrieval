package coding.challenge.core.context

import java.util.Locale

/**
 * Contains information about execution context fo the current request.
 */
class ExecutionContext {

    /**
     * Language code specified in the request or default locale language.
     */
    var language: String? = null
        get() = field ?: Locale.getDefault().language

    companion object {

        private val CONTEXT: ThreadLocal<ExecutionContext> = ThreadLocal.withInitial { ExecutionContext() }

        fun get(): ExecutionContext {
            return CONTEXT.get()
        }

        fun clear() {
            CONTEXT.remove()
        }

    }

}
