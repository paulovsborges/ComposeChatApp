package com.pvsb.composechatapp.presentation.chat

import com.pvsb.composechatapp.domain.entity.MessageModel

data class ChatState(
    val messages: List<MessageModel> = emptyList(),
    val isLoading: Boolean = false
)