@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.sign.domain

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SignerTest {

    private val signer = Signer(PRIVATE_KEY)

    @Test
    fun `주어진 개인키를 사용, 객체를 JWT로 서명한다`() {
        data class TestObject(val key: String)

        val testObjectAsJson = TestObject("value")
            .let { ObjectMapper().writeValueAsString(it).toByteArray() }

        val jwtPayload = signer.sign(testObjectAsJson.decodeToString())
            .substringAfter('.')
            .substringBeforeLast('.')

        assertThat(jwtPayload).asBase64Decoded().isEqualTo(testObjectAsJson)
    }

    companion object {
        private val PRIVATE_KEY = PKCS8PEMKey(
            """-----BEGIN PRIVATE KEY-----
MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQC92qcfpwgUADIL
QjRgrYddJYOQrapgEW7UDrrcizJ11di36VdAnAeQhfFKPEnZ+Khq2XGJXl5CKCql
aWJl1XhxkJtFQtBHzVPg/TkrpMb2Zym9ySjHnkTFObllh5GqFU2J573vwWAHm81s
RtADcYje4+l6UMWgD3P+4JVXerheGtq8I4Kdp+ePujg3Myzkjr7mdBvTD3R4VU6s
FBcxDg4OkDc0DDZRvBJ+nzVjt7pALiOvskYeKoqWYTHXpOMzeh+siWoijv7BSlFH
L53qjv2orhOKA1ugwxoxjJ2rw5rfJMy1gyuevXzsC9wXE6Z5pxKskf7V5K6eSgwC
TV1OwRBtAgMBAAECggEAYtkXLqm+0PsREz+TGcr/sYW4rHlfPcmV1J8et3lZ+nQu
9AJvxVn3rk2uxaICKZUHyyoPrSAPH47eNWVbL/4nSamqzjLtUpUbWQc+3vRaCyzH
VOMJcnV7BNz7JFujgdS5nFWmVNBv+UYrfHaaE1DnnHU5uM+ZNa8WC9xOpU9l8shO
uHmrBSFWfs3bhg9MA4PBKyam6I5VrYTu2I0NUKg7/pqiNPjLc0oOFQfTcbFF/bBQ
EFDo7KQuN8kQ6eNSt0ap0c17NP+kzvRbsx8pHJLDGdDpPMhAfR/HAkjYDHi3tGu0
gOdXGEKeeBMvOcuaXnt6lHnO6Sp7Lvn6sBUlw4ixAQKBgQDol6sgeKsGmfLYVSQh
tMI75zBGOeL+M0ElOrAqQtcXxkdtQA9vMRL2MMby1vx3Q/dL+nla6hLrsP7vYs6W
zohmwkPpKFOwjancDuzwAGPW5Oe7hSM1wgS9GmDulrlBrjlTVhwMYMAS9CrQ9s+6
HYNImJH6Qy/9wRixrFiOLWxmDQKBgQDQ9egoxkI4NoXM4T4J65KtaM+12o5aTTjN
Itri0cDtjuSf4c8Ryq+NayKbKGNGWqc/LXMwVbXL+KP9vc33U5pmbeKsZfYHZ1cX
97uK8KqqjJYcIu80EcnfF9yqSSWgSomqKg+wIhzfuOAxhDURLnc9NjGJ9cX1gDpr
HGImzJgb4QKBgQDUwN8kjZwLJsiuv3qUZk6BEuDia9LJqMJ/Nmar0YYEVjdBpKY5
lE7+6PEicipvQbk0Q4G7+n6iHr2i5OOJqJO2Qx5xaJTc20gzZWevxPuONsYNjiP9
6WICQ93EgBQnEyGWrB5t6BXpgHSlvg2W9aX/wqdMnyaH25gl9TrvmnccBQJ/K0K9
zbM+AmVHTiNTtLoQo9YPKpcU4Z0bJV0R8+N5DbWHn5IgTI6pyZ1kjLrP0sDx3yKJ
tngIKzJOwTmKaqnkTVBynT5+sQmz/kJLkXhB3Q0+BpU08JK+OHG4PYjnQUgtOA19
MfneAHNF9vpoAiaciQ+vMeCwGyNw9LvrlhzFYQKBgHflIhIL1OW+hRTM6c6eJAyd
/kWWhBFasIoKIvEM4xpL/z1fHQOcOf3yRXlvGdE3b1N/a5zbFTk1ne1Vvrz38vDI
KKCh/vU8KGYKqgdHosOY8OuDtdoeuOK1mALzGISmOEg6SsmIhieRVD+L8/tyPBGm
xC/YpPH8UAOqabX6ggSu
-----END PRIVATE KEY-----
"""
        ).toPrivateKey()
    }
}
