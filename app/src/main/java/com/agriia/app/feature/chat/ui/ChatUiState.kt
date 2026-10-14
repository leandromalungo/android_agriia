package com.agriia.app.feature.chat.ui

import com.agriia.app.feature.chat.domain.model.ChatMessage
import com.agriia.app.feature.chat.domain.model.Conversation

data class ChatUiState(
    val currentConversationId: String? = null,
    val conversations: List<Conversation> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val userName: String = "Leandro",
    val currentModelName: String = "agriia 1.0 · Pensamento",
    val inputText: String = "",
    val isTemporaryChat: Boolean = false,
    val isStreaming: Boolean = false,
    val showUpgradeBanner: Boolean = true,
    val showProjectsDialog: Boolean = false,
    val showArtifactsDialog: Boolean = false,
    val showModelSelectorDialog: Boolean = false,
    val toastMessage: String? = null
)
