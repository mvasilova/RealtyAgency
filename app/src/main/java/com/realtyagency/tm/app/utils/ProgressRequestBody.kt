package com.realtyagency.tm.app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody(
    private val file: File,
    private val progressFileManager: ProgressFileManager
) : RequestBody() {

    private val mainUiScope by lazy { CoroutineScope(SupervisorJob() + Dispatchers.Main) }

    override fun contentType(): MediaType? {
        return MediaType.parse("multipart/form-data")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {

        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var uploaded: Long = 0

        FileInputStream(file).use { fis ->
            var read: Int
            while (fis.read(buffer).also { read = it } != -1) {
                mainUiScope.launch {
                    progressFileManager.addProgress(file, uploaded)
                }

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}

class ProgressFileManager(files: List<File>, private val listener: (Int) -> Unit) {
    private val totalFilesSize = files.sumBy { it.length().toInt() }
    private val mapProgress = mutableMapOf<File, Long>()

    fun addProgress(file: File, uploaded: Long) {
        mapProgress[file] = uploaded

        val commonProgress = (100.0 * mapProgress.values.sum() / totalFilesSize).toInt()
        listener.invoke(commonProgress)
    }
}