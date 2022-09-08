package com.bbg.aiservicemock.repository.model

import org.tensorflow.lite.Interpreter

interface IModelRepository {
    fun getModel(modelFile : String): Interpreter?
}