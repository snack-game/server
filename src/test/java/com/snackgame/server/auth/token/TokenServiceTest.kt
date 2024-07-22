package com.snackgame.server.auth.token

import com.snackgame.server.auth.exception.RefreshTokenExpiredException
import com.snackgame.server.auth.exception.TokenExpiredException
import com.snackgame.server.auth.token.domain.RefreshTokenRepository
import com.snackgame.server.auth.token.util.JwtProvider
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean

@Suppress("NonAsciiCharacters")
@ServiceTest
internal class TokenServiceTest {
    @Autowired
    private lateinit var memberAccountService: MemberAccountService

    @Autowired
    @InjectMocks
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Test
    fun `토큰을 발급하면 리프레시 토큰이 발급 및 저장된다`() {
        val created = memberAccountService.createGuest()

        val refreshToken = tokenService.issueFor(created.id).refreshToken

        assertThat(refreshTokenRepository.findAll()
            .map { it.token }).first()
            .isEqualTo(refreshToken)
    }

    @Test
    fun `리프레시 토큰을 삭제한다`() {
        val created = memberAccountService.createGuest()
        val refreshToken = tokenService.issueFor(created.id).refreshToken

        tokenService.delete(refreshToken)

        assertThat(refreshTokenRepository.findAll()).isEmpty()
    }


    @MockBean(name = "refreshTokenProvider")
    @Autowired
    private lateinit var refreshTokenProvider: JwtProvider

    @Test
    fun `리프레시 토큰도 만료되었다면 토큰을 재발급할 수 없다`() {
        given(refreshTokenProvider.validate(any())).willThrow(TokenExpiredException())
        val created = memberAccountService.createGuest()
        val refreshToken = tokenService.issueFor(created.id).refreshToken

        assertThatThrownBy { tokenService.reissueFrom(refreshToken) }
            .isInstanceOf(RefreshTokenExpiredException::class.java)
    }
}
