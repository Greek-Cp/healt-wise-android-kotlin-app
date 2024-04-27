package com.dicoding.asclepius.view.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

object ImageUtils {

    fun byteArrayToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream).also {
                inputStream?.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        ByteArrayOutputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            return outputStream.toByteArray()
        }
    }
}
