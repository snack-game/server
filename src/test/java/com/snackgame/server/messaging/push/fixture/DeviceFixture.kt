package com.snackgame.server.messaging.push.fixture

import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.messaging.push.domain.Device
import com.snackgame.server.support.fixture.FixtureSaver

object DeviceFixture {

    @JvmStatic
    fun 땡칠_Device() = Device(
        땡칠().id,
        "esAh_qDD2k5loDfyMtG_AQ:APA91bEuSTEH8S9YlOAXih9sslV7SN1kb0Tk2WMBsmx7j3vioyLkMUxG8CFIyj" +
                "5UDopwbdNX4xctRVEh1NQstPliWmEq2TIL4-Rl3oHEllVu0Ni0USz-hps"
    )

    @JvmStatic
    fun 정환_Device() = Device(
        정환().id,
        "cFwP3VYHh0eyoXkyY9MwJr:APA91bHX5YiVXuIvi-pLDqNHcJMhl7hKrqLTC7opFMbzj4CsXrg1wu2ayG_" +
                "LFVREto678gQdWGUnmBXwKEpEJTfXheX0Fz83xwqDzVrKvXF3H5t07XXU6e-boq8JnZVCbs6NB_VfGRh8"
    )

    @JvmStatic
    fun saveAll() {
        FixtureSaver.save(
            땡칠_Device(),
            정환_Device()
        )
    }

}
