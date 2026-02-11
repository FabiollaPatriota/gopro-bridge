package com.fabilea.goprobridge.domain

enum class GoProCommand(
    val label: String,
    val endpointPath: String,
) {
    SHUTTER("Disparar / Parar gravação", "/bacpac/SH?t=%s&p=%%01"),
    MODE_VIDEO("Modo Vídeo", "/camera/CM?t=%s&p=%%00"),
    MODE_PHOTO("Modo Foto", "/camera/CM?t=%s&p=%%01"),
    POWER_OFF("Desligar câmera", "/bacpac/PW?t=%s&p=%%00"),
}
