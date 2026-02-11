package com.fabilea.goprobridge.data

import com.fabilea.goprobridge.domain.GoProCommand
import com.fabilea.goprobridge.domain.GoProStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoProRepository(
    private val api: GoProLegacyApi = GoProLegacyApi(),
) {
    suspend fun sendCommand(
        command: GoProCommand,
        password: String,
        cameraSsid: String,
    ): GoProStatus = withContext(Dispatchers.IO) {
        val result = api.execute(command, password)
        GoProStatus(
            connected = result.successful,
            cameraSsid = cameraSsid,
            lastResponseCode = result.responseCode,
            lastMessage = result.message,
        )
    }
}
