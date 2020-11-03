package com.leroylu.struct.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
object FileOperator {

    fun saveUserIcon(ctx: Context, file: File): File {
        val dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val saved = File(file, "${System.currentTimeMillis()}.jpg")
        return file.copyTo(saved)
    }

    suspend fun saveUserIcon(ctx: Context, bitmap: Bitmap): File = withContext(Dispatchers.IO) {
        val dir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val saved = File(dir, "${System.currentTimeMillis()}.jpg")

        val outputStream = FileOutputStream(saved)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        saved
    }
}