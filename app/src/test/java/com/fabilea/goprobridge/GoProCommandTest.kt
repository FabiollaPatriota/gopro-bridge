package com.fabilea.goprobridge

import com.fabilea.goprobridge.domain.GoProCommand
import org.junit.Assert.assertEquals
import org.junit.Test

class GoProCommandTest {

    @Test
    fun `command endpoint keeps legacy parameter bytes`() {
        val password = "abc123"

        assertEquals("/bacpac/SH?t=abc123&p=%01", GoProCommand.SHUTTER.endpointPath.format(password))
        assertEquals("/camera/CM?t=abc123&p=%00", GoProCommand.MODE_VIDEO.endpointPath.format(password))
        assertEquals("/camera/CM?t=abc123&p=%01", GoProCommand.MODE_PHOTO.endpointPath.format(password))
        assertEquals("/bacpac/PW?t=abc123&p=%00", GoProCommand.POWER_OFF.endpointPath.format(password))
    }
}
