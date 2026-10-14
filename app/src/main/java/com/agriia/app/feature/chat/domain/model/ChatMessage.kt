package com.agriia.app.feature.chat.domain.model

enum class ChatRole {
    USER, ASSISTANT
}

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val role: ChatRole,
    val content: String,
    val timestamp: Long,
    val isStreaming: Boolean = false,
    val isError: Boolean = false
)
