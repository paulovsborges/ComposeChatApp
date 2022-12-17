package com.pvsb.composechatapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvsb.composechatapp.datasource.service.http.MessageService
import com.pvsb.composechatapp.datasource.service.websocket.ChatSocketService
import com.pvsb.composechatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val socketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _chatState = mutableStateOf(ChatState())
    val chatState: State<ChatState> = _chatState

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun initChat() {
        getAllMessages()
        initSession()
    }

    private fun initSession() {
        savedStateHandle.get<String>("username")?.let { userName ->
            viewModelScope.launch {
                val result = socketService.initSession(userName)

                when (result) {
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                    is Resource.Success -> {
                        socketService.observeMessages().onEach { message ->
                            val newList = _chatState.value.messages.toMutableList().apply {
                                add(0, message)
                            }

                            _chatState.value = chatState.value.copy(messages = newList)
                        }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }

    fun onMessageChanged(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            socketService.closeSession()
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            _chatState.value = _chatState.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _chatState.value = _chatState.value.copy(
                messages = result,
                isLoading = false
            )
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) socketService.sendMessage(messageText.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

}