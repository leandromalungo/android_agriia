package com.agriia.app.feature.chat.ui

import com.agriia.app.core.designsystem.components.AgriiaTopTab
import com.agriia.app.feature.chat.domain.model.ChatAttachment
import com.agriia.app.feature.chat.domain.model.ChatMessage
import com.agriia.app.feature.chat.domain.model.Conversation

data class ChatUiState(
    val currentConversationId: String? = null,
    val conversations: List<Conversation> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val userName: String = "Leandro",
    val currentModelName: String = "Default",
    val inputText: String = "",
    val searchQuery: String = "",
    val isTemporaryChat: Boolean = false,
    val isStreaming: Boolean = false,
    val selectedTopTab: AgriiaTopTab = AgriiaTopTab.CHAT,
    val showUpgradeBanner: Boolean = false,
    val showProjectsDialog: Boolean = false,
    val showArtifactsDialog: Boolean = false,
    val showModelSelectorDialog: Boolean = false,
    val showAttachmentSheet: Boolean = false,
    val showMapDialog: Boolean = false,
    val showRealtimeVoiceModal: Boolean = false,
    val pendingAttachment: ChatAttachment? = null,
    val toastMessage: String? = null
)
