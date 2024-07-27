package com.snackgame.server.auth.token.support

import com.google.gson.JsonParser
import com.snackgame.server.auth.token.util.JwtProvider
import org.springframework.core.MethodParameter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.util.WebUtils
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class OptionalGuestArgumentResolver(
    private val accessTokenProvider: JwtProvider,
    private val guestResolver: GuestResolver<*>
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(OptionalGuest::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        @Nullable mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        @Nullable binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        val accessToken = extractCookieFrom(request, accessTokenProvider.canonicalName)

        if (!accessToken.isNullOrBlank() && accessToken.matches(JWT_PATTERN)) {
            val guestId = parseSubjectOf(accessToken)
            if (guestId != null) {
                return guestResolver.resolve(guestId)
            }
        }
        return null
    }

    private fun extractCookieFrom(request: HttpServletRequest, cookieName: String): String? {
        val cookie = WebUtils.getCookie(request, cookieName)
        return cookie?.value
    }

    private fun parseSubjectOf(accessToken: String): Long? {
        val encodedPayload = accessToken.substringAfter(".").substringBeforeLast(".")
        val decoded = String(Base64.getDecoder().decode(encodedPayload))
        val jsonElement = JsonParser.parseString(decoded)
        if (jsonElement.isJsonObject) {
            return jsonElement.asJsonObject["sub"].asLong
        }
        return null
    }

    companion object {
        val JWT_PATTERN = Regex("^[A-Za-z0-9_-]+\\.([A-Za-z0-9_-]+\\.?)+[A-Za-z0-9_-]+$")
    }
}
