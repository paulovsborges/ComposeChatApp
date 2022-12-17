package com.pvsb.composechatapp.datasource.service.websocket

import com.pvsb.composechatapp.domain.entity.MessageModel
import com.pvsb.composechatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        userName: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<MessageModel>

    suspend fun closeSession()


    companion object {
        const val BASE_URL = "ws://0.0.0.0:8080"
        const val PHILIP_BASE_URL = "ws://10.0.2.2:8080"
        const val MY_IP_BASE_URL = "ws://192.168.15.4:8080"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocketRoute : Endpoints("$MY_IP_BASE_URL/chat-socket")
    }
}