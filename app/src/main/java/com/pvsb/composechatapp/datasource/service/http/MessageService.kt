package com.pvsb.composechatapp.datasource.service.http

import com.pvsb.composechatapp.domain.entity.MessageModel

interface MessageService {
    suspend fun getAllMessages(): List<MessageModel>

    companion object {
        const val BASE_URL = "http://0.0.0.0:8080"
        const val PHILIP_BASE_URL = "http://10.0.2.2:8080"
        const val MY_IP_BASE_URL = "http://192.168.15.4:8080"
    }

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$MY_IP_BASE_URL/messages")
    }
}