package com.fabilea.goprobridge.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class GoProApi {

    private suspend fun call(url: String): String = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"
            connection.connect()

            val code = connection.responseCode
            "HTTP $code"
        } catch (e: Exception) {
            "Falha: ${e.message}"
        }
    }

    suspend fun status() =
        call("http://10.5.5.9/gp/gpControl/status")

    suspend fun setPhotoMode() =
        call("http://10.5.5.9/gp/gpControl/command/mode?p=2")

    suspend fun setVideoMode() =
        call("http://10.5.5.9/gp/gpControl/command/mode?p=1")

    suspend fun takePhoto() =
        call("http://10.5.5.9/gp/gpControl/command/shutter?p=1")

    suspend fun startVideo() =
        call("http://10.5.5.9/gp/gpControl/command/shutter?p=1")

    suspend fun stopVideo() =
        call("http://10.5.5.9/gp/gpControl/command/shutter?p=0")
}
