package com.pvsb.composechatapp.datasource.service.http

import com.pvsb.composechatapp.datasource.dto.MessageDTO
import com.pvsb.composechatapp.domain.entity.MessageModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class MessageServiceImpl(
    private val client: HttpClient
) : MessageService {

    override suspend fun getAllMessages(): List<MessageModel> {
        return try {
            client.get<List<MessageDTO>>(MessageService.Endpoints.GetAllMessages.url).map { it.toModel() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}