package com.fabilea.goprobridge.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

class MjpegStream(private val input: InputStream) {

    fun readFrame(): Bitmap? {
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(1024)

        while (true) {
            val len = input.read(data)
            if (len == -1) return null

            buffer.write(data, 0, len)
            val bytes = buffer.toByteArray()

            for (i in 0 until bytes.size - 1) {
                if (bytes[i] == 0xFF.toByte() && bytes[i + 1] == 0xD8.toByte()) {
                    for (j in i + 2 until bytes.size - 1) {
                        if (bytes[j] == 0xFF.toByte() && bytes[j + 1] == 0xD9.toByte()) {
                            val image = bytes.copyOfRange(i, j + 2)
                            return BitmapFactory.decodeByteArray(image, 0, image.size)
                        }
                    }
                }
            }
        }
    }
}
