package com.pvsb.composechatapp.datasource.dto

import com.pvsb.composechatapp.domain.entity.MessageModel
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class MessageDTO(
    val text: String,
    val timeStamp: Long,
    val userName: String,
    val id: String
) {
    fun toModel(): MessageModel {
        val date = Date(timeStamp)
        val formattedDate = DateFormat.getDateInstance().format(date)

        return MessageModel(
            text = this.text,
            time = formattedDate,
            userName = userName
        )
    }
}
