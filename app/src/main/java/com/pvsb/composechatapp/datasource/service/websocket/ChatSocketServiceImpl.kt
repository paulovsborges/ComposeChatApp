package com.pvsb.composechatapp.datasource.service.websocket

import com.pvsb.composechatapp.datasource.dto.MessageDTO
import com.pvsb.composechatapp.domain.entity.MessageModel
import com.pvsb.composechatapp.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
    private val client: HttpClient
) : ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(userName: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocketRoute.url}?userName=$userName")
            }

            if (socket?.isActive == true) Resource.Success(Unit) else Resource.Error("Unable to establish a connection.")

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<MessageModel> {
        return try {
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }?.map {
                val json = (it as? Frame.Text)?.readText()
                val message = Json.decodeFromString<MessageDTO>(json ?: "")
                message.toModel()
            } ?: flowOf()
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}