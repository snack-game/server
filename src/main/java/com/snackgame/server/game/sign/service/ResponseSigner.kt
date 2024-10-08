package com.snackgame.server.game.sign.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.snackgame.server.game.sign.domain.Signer
import com.snackgame.server.game.sign.service.dto.SignedResponse
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ResponseSigner(
    val signer: Signer,
    val objectMapper: ObjectMapper
) : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.hasMethodAnnotation(Signed::class.java)
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return body?.let {
            val jsonContent = objectMapper.writeValueAsString(it)
            SignedResponse(it, signer.sign(jsonContent))
        }
    }
}
