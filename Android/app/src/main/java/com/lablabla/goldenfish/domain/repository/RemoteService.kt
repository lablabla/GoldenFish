package com.lablabla.goldenfish.domain.repository

import com.lablabla.goldenfish.domain.model.Zone
import com.lablabla.goldenfish.util.Resource

interface RemoteService {

    suspend fun sync()

    fun setCallback(callback: ()->Unit)
}