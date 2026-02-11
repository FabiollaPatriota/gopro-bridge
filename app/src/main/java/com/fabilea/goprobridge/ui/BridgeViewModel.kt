package com.fabilea.goprobridge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabilea.goprobridge.data.GoProRepository
import com.fabilea.goprobridge.domain.GoProCommand
import com.fabilea.goprobridge.domain.GoProStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BridgeViewModel(
    private val repository: GoProRepository = GoProRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(BridgeUiState())
    val uiState: StateFlow<BridgeUiState> = _uiState.asStateFlow()

    fun updateSsid(value: String) {
        _uiState.update { it.copy(ssid = value) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(wifiPassword = value) }
    }

    fun updateCommand(command: GoProCommand) {
        _uiState.update { it.copy(selectedCommand = command) }
    }

    fun sendCommand() {
        val snapshot = _uiState.value
        if (snapshot.wifiPassword.isBlank()) {
            _uiState.update {
                it.copy(
                    status = GoProStatus(
                        connected = false,
                        cameraSsid = snapshot.ssid,
                        lastMessage = "Informe a senha Wi-Fi/BacPac da câmera.",
                    ),
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val status = repository.sendCommand(
                command = snapshot.selectedCommand,
                password = snapshot.wifiPassword,
                cameraSsid = snapshot.ssid,
            )
            _uiState.update { it.copy(loading = false, status = status) }
        }
    }
}
