package com.bbg.aiservicemock.datasource

import com.bbg.aiservicemock.entity.JammerParameter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class JammerDatasource(private val parameter: JammerParameterApi, private val refreshIntervalMs: Long = 5_000) {
    val latestJammerParameter: Flow<List<JammerParameter>> = flow {
        while (true) {
            val latestParameter = parameter.fetchLatestParameter()
            emit(latestParameter)
            delay(refreshIntervalMs)
        }
    }
}

interface JammerParameterApi {
    suspend fun fetchLatestParameter() : List<JammerParameter>
}