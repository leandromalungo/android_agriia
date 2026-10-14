package com.agriia.app.feature.chat.domain.model

data class Conversation(
    val id: String,
    val title: String,
    val createdAt: Long,
    val updatedAt: Long,
    val modelName: String = "agriia 1.0 · Pensamento",
    val isPinned: Boolean = false
)
