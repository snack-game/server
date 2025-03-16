package com.snackgame.server.rank.provoke

import com.snackgame.server.messaging.push.service.PushService
import com.snackgame.server.messaging.push.service.dto.NotificationRequest
import org.springframework.stereotype.Service
import java.util.concurrent.Future

@Service
class ProvokeService(
    val pushService: PushService,
    val provokeRepository: ProvokeRepository
) {

    fun reserveProvoking(ownerId: Long, sessionId: Long) {
        val provoke = Provoke(ownerId, sessionId)
        provoke.getReady()
        provokeRepository.save(provoke)
    }

    fun provoke(targetId: Long, name: String): Future<*> {
        return pushService.sendPushMessage(
            NotificationRequest(
                TITLE,
                "누군가가 ${name}님의 기록을 깼어요. 이대로 끝내실 건 아니죠?"
            ),
            targetId
        )
    }

    fun checkStatus(ownerId: Long, sessionId: Long): Boolean {
        val found = provokeRepository.findProvokeByOwnerIdAndSessionId(ownerId, sessionId)
        return found.isUpdated()
    }

    companion object {
        private const val TITLE = "나야 도발장"
    }
}
