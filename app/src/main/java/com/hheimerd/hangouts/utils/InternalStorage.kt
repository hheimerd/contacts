package com.hheimerd.hangouts.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.io.IOException

object InternalStorage {

    const val ImagesDir = "images"

    fun savePhoto(context: Context, filename: String, bitmap: Bitmap): Boolean {
        return try {
            context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException()
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getPhoto(context: Context, filename: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            Log.d("getPhoto", filename)
            val file = context.filesDir
                .listFiles(FileFilter { it.canRead() && it.isFile && it.name.startsWith(filename) })
                ?.firstOrNull()
            Log.d("getPhoto", file.toString())

            if (file == null)
                return@withContext null

            val bytes = file.readBytes()
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}
