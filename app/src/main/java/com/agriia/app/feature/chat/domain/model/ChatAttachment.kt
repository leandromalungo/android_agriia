package com.agriia.app.feature.chat.domain.model

enum class AttachmentType {
    FILE,
    IMAGE,
    CAMERA,
    MAP
}

data class ChatAttachment(
    val type: AttachmentType,
    val title: String,
    val subtitle: String? = null,
    val dataUri: String? = null
)
