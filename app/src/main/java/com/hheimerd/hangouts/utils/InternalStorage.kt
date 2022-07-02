package com.hheimerd.hangouts.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter
import java.io.IOException
import javax.inject.Inject

class InternalStorage @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
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

        fun getPhoto(context: Context, filename: String): Bitmap? {
            Log.d("getPhoto", filename)
            val file = context.filesDir
                .listFiles(FileFilter { it.canRead() && it.isFile && it.name.startsWith(filename) })
                ?.firstOrNull()

            if (file == null)
                return null

            val bytes = file.readBytes()
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        }

        fun deletePhoto(context: Context, filename: String) {
            val file = context.filesDir
                .listFiles(FileFilter { it.canRead() && it.isFile && it.name.startsWith(filename) })
                ?.firstOrNull()

            if (file == null)
                return

            file.delete()
        }
    }

    fun savePhoto(filename: String, bitmap: Bitmap): Boolean
    {
        return savePhoto(context, filename, bitmap);
    }

    fun getPhoto(filename: String): Bitmap? {
        return getPhoto(context, filename);
    }

    fun deletePhoto(filename: String) {
        deletePhoto(context, filename);
    }
}
