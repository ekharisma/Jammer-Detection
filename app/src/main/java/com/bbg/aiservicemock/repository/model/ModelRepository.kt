package com.bbg.aiservicemock.repository.model

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import com.bbg.aiservicemock.MainApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ModelRepository : IModelRepository {
    override fun getModel(modelFile: String): Interpreter? {
        val file = runBlocking(Dispatchers.IO) {
            loadModelFile(MainApplication.applicationContext().assets, modelFile)
        }
        return file?.let { Interpreter(it) }
    }

    @Throws(IOException::class)
    private fun loadModelFile(assets: AssetManager, modelFile: String): MappedByteBuffer? {
        val fileDescriptor: AssetFileDescriptor = assets.openFd(modelFile)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset: Long = fileDescriptor.startOffset
        val declaredLength: Long = fileDescriptor.declaredLength
        return runBlocking(Dispatchers.IO) {
            getModelBuffer(fileChannel, startOffset, declaredLength)
        }
    }

    private fun getModelBuffer(fileChannel : FileChannel, startOffset : Long, declaredLength : Long): MappedByteBuffer? {
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

}