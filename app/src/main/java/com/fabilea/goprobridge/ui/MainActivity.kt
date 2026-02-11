package com.fabilea.goprobridge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fabilea.goprobridge.domain.GoProCommand

class MainActivity : ComponentActivity() {
    private val viewModel: BridgeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                BridgeScreen(
                    uiState = uiState,
                    onSsidChange = viewModel::updateSsid,
                    onPasswordChange = viewModel::updatePassword,
                    onCommandChange = viewModel::updateCommand,
                    onSendClick = viewModel::sendCommand,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BridgeScreen(
    uiState: BridgeUiState,
    onSsidChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCommandChange: (GoProCommand) -> Unit,
    onSendClick: () -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "GoPro Bridge (Hero 3/3+/4/4+)",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Conecte no Wi‑Fi legado da câmera e envie comandos diretos para 10.5.5.9.",
                style = MaterialTheme.typography.bodyMedium,
            )

            OutlinedTextField(
                value = uiState.ssid,
                onValueChange = onSsidChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("SSID da câmera") },
                placeholder = { Text("GOPRO-XXXX") },
                singleLine = true,
            )

            OutlinedTextField(
                value = uiState.wifiPassword,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha Wi‑Fi/BacPac") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = uiState.selectedCommand.label,
                    onValueChange = {},
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    label = { Text("Comando") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    GoProCommand.entries.forEach { command ->
                        DropdownMenuItem(
                            text = { Text(command.label) },
                            onClick = {
                                onCommandChange(command)
                                expanded = false
                            },
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSendClick,
                    enabled = !uiState.loading,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Text("Enviar comando")
                }
                if (uiState.loading) {
                    CircularProgressIndicator()
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("Resposta", style = MaterialTheme.typography.titleMedium)
                    Text("Conectado: ${if (uiState.status.connected) "sim" else "não"}")
                    Text("SSID: ${uiState.status.cameraSsid.ifBlank { "-" }}")
                    Text("HTTP: ${uiState.status.lastResponseCode?.toString() ?: "-"}")
                    Text(uiState.status.lastMessage.ifBlank { "Nenhum comando enviado ainda." })
                }
            }
        }
    }
}
