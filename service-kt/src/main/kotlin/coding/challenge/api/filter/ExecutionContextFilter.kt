package coding.challenge.api.filter

import coding.challenge.core.context.ExecutionContext
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Initializes [ExecutionContext] for the request thread based on the request information.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ExecutionContextFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            ExecutionContext.get().language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
            filterChain.doFilter(request, response)
        } finally {
            ExecutionContext.clear()
        }
    }

}
