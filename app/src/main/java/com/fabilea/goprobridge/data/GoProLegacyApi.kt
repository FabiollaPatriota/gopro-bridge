package com.fabilea.goprobridge.data

import com.fabilea.goprobridge.domain.GoProCommand
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class GoProLegacyApi(
    private val baseUrl: String = "http://10.5.5.9",
    private val client: OkHttpClient = OkHttpClient(),
) {
    suspend fun execute(command: GoProCommand, password: String): ApiResult {
        val path = command.endpointPath.format(password)
        val request = Request.Builder()
            .url("$baseUrl$path")
            .get()
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                val body = response.body?.string().orEmpty()
                ApiResult(
                    successful = response.isSuccessful,
                    responseCode = response.code,
                    message = body.ifBlank { "Sem conteúdo retornado pela câmera." },
                )
            }
        } catch (exception: IOException) {
            ApiResult(
                successful = false,
                responseCode = null,
                message = "Falha de rede: ${exception.message}",
            )
        }
    }
}

data class ApiResult(
    val successful: Boolean,
    val responseCode: Int?,
    val message: String,
)
