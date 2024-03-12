package com.snackgame.server.common.file

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class S3FileUploaderTest {

    /*
    UUID는 충돌할 확률이 극히 낮습니다.
    거기에 일반적으로 충돌할 확률이 낮은 hashCode를 첨가하니, 더 확률이 희박해집니다.
    그럼에도 충돌한다면 어떨까요?
    그래도 프로필 이미지는 중요하지 않으므로 괜찮다고 생각합니다. 로또 사러 가야죠.
     */

    @Test
    fun `UUID는 충돌할 확률이 극히 낮다`() {
        assertThat(UUID.randomUUID()).isNotEqualTo(UUID.randomUUID())
    }

    @Nested
    inner class `String의 hashCode는` {
        @Test
        fun `일반적으로 다르다`() {
            assertThat("image1234".hashCode()).isNotEqualTo("image1235".hashCode())
        }

        @Test
        fun `가끔 충돌할 수도 있다`() {
            assertThat("S.ME".hashCode()).isEqualTo("RN.E".hashCode())
        }
    }

    @Test
    fun `UUID와 String의 hashCode를 합치면 충돌확률이 더더욱 낮다`() {
        assertThat(generateUniqueId("imageA")).isNotEqualTo(generateUniqueId("imageB"))
    }

    private fun generateUniqueId(string: String): String {
        val randomUUID = UUID.randomUUID().toString()
        return "${randomUUID.replace("-", "")}-${string.hashCode()}"
    }
}
