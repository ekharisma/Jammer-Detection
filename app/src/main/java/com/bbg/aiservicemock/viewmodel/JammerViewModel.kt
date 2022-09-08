package com.bbg.aiservicemock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbg.aiservicemock.datasource.JammerDatasource
import com.bbg.aiservicemock.entity.JammerParameter
import com.bbg.aiservicemock.logger.Logger
import kotlinx.coroutines.launch

class JammerViewModel(private val jammerDatasource: JammerDatasource) : ViewModel() {
    private val logger = Logger("JammerViewModel")
//    private val jammerParameterEntity = JammerParameter()
    init {
        viewModelScope.launch {
            jammerDatasource.latestJammerParameter.collect{ parameters ->
                logger.info("value from kotlin flow: $parameters ")
            }
        }
    }
}