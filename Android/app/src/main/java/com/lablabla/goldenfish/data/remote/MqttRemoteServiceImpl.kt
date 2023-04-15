package com.lablabla.goldenfish.data.remote

import com.lablabla.goldenfish.domain.repository.RemoteService

class MqttRemoteServiceImpl: RemoteService {

    private var callback: (() -> Unit)? = null

    override suspend fun sync() {

    }

    override fun setCallback(callback: () -> Unit) {
        this.callback = callback
    }
}