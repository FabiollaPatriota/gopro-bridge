package com.fabilea.goprobridge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fabilea.goprobridge.ui.theme.GoProBridgeTheme
import com.fabilea.goprobridge.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoProBridgeTheme {
                val viewModel: MainViewModel = viewModel()
                val status by viewModel.result.collectAsState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("GoPro Bridge", style = MaterialTheme.typography.headlineMedium)
                    Text(status)

                    Button(onClick = { viewModel.status() }) {
                        Text("📡 Status da GoPro")
                    }

                    Button(onClick = { viewModel.takePhoto() }) {
                        Text("📷 Tirar Foto")
                    }

                    Button(onClick = { viewModel.startVideo() }) {
                        Text("🔴 Iniciar Vídeo")
                    }

                    Button(onClick = { viewModel.stopVideo() }) {
                        Text("⏹ Parar Vídeo")
                    }
                }
            }
        }
    }
}
