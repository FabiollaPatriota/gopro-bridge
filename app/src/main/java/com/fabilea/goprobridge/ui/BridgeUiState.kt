package com.fabilea.goprobridge.ui

import com.fabilea.goprobridge.domain.GoProCommand
import com.fabilea.goprobridge.domain.GoProStatus

data class BridgeUiState(
    val ssid: String = "",
    val wifiPassword: String = "",
    val selectedCommand: GoProCommand = GoProCommand.SHUTTER,
    val loading: Boolean = false,
    val status: GoProStatus = GoProStatus(),
)
