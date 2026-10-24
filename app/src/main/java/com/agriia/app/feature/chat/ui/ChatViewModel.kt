package com.agriia.app.feature.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agriia.app.core.datastore.UserPreferencesRepository
import com.agriia.app.feature.chat.domain.model.ChatMessage
import com.agriia.app.feature.chat.domain.model.ChatRole
import com.agriia.app.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var messagesJob: Job? = null
    private var streamingJob: Job? = null
    private var conversationsJob: Job? = null
    private val _searchQuery = MutableStateFlow("")

    init {
        // Observa preferências do usuário (Nome e Modelo)
        viewModelScope.launch {
            userPreferencesRepository.userNameFlow.collect { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.userEmailFlow.collect { email ->
                _uiState.update { it.copy(userEmail = email) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.farmNameFlow.collect { farm ->
                _uiState.update { it.copy(farmName = farm) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.userPhotoUrlFlow.collect { photoUrl ->
                _uiState.update { it.copy(userPhotoUrl = photoUrl) }
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.currentModelFlow.collect { model ->
                _uiState.update { it.copy(currentModelName = model) }
            }
        }

        viewModelScope.launch {
            chatRepository.seedInitialMockConversationsIfEmpty()
        }

        observeConversations()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeConversations() {
        conversationsJob?.cancel()
        conversationsJob = viewModelScope.launch {
            _searchQuery
                .debounce(150L)
                .flatMapLatest { query ->
                    val trimmed = query.trim()
                    if (trimmed.isBlank()) {
                        chatRepository.getRecentConversations()
                    } else {
                        chatRepository.searchConversations(trimmed)
                    }
                }
                .collect { list ->
                    _uiState.update { it.copy(conversations = list) }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onInputTextChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onSelectConversation(conversationId: String) {
        _uiState.update {
            it.copy(
                currentConversationId = conversationId,
                isTemporaryChat = false
            )
        }
        observeMessages(conversationId)
    }

    fun onStartNewChat() {
        messagesJob?.cancel()
        streamingJob?.cancel()
        _uiState.update {
            it.copy(
                currentConversationId = null,
                messages = emptyList(),
                inputText = "",
                isStreaming = false
            )
        }
    }

    fun onDeleteConversation(conversationId: String) {
        viewModelScope.launch {
            chatRepository.deleteConversation(conversationId)
            if (_uiState.value.currentConversationId == conversationId) {
                onStartNewChat()
            }
        }
    }

    fun onToggleTemporaryChat() {
        val next = !_uiState.value.isTemporaryChat
        _uiState.update {
            it.copy(
                isTemporaryChat = next,
                toastMessage = if (next) "Bate-papo temporário ativado" else "Bate-papo normal ativado"
            )
        }
    }

    fun onSelectTopTab(tab: com.agriia.app.core.designsystem.components.AgriiaTopTab) {
        _uiState.update { it.copy(selectedTopTab = tab) }
    }

    fun onDismissToast() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onDismissUpgradeBanner() {
        _uiState.update { it.copy(showUpgradeBanner = false) }
    }

    fun onShowProjectsDialog(show: Boolean) {
        _uiState.update { it.copy(showProjectsDialog = show) }
    }

    fun onShowArtifactsDialog(show: Boolean) {
        _uiState.update { it.copy(showArtifactsDialog = show) }
    }

    fun onShowModelSelectorDialog(show: Boolean) {
        _uiState.update { it.copy(showModelSelectorDialog = show) }
    }

    fun onShowAttachmentSheet(show: Boolean) {
        _uiState.update { it.copy(showAttachmentSheet = show) }
    }

    fun onShowMapDialog(show: Boolean) {
        _uiState.update { it.copy(showMapDialog = show) }
    }

    fun onShowSettingsScreen(show: Boolean) {
        _uiState.update { it.copy(showSettingsScreen = show) }
    }

    fun onShowNotificationsScreen(show: Boolean) {
        _uiState.update { it.copy(showNotificationsScreen = show) }
    }

    fun onShowProfileConfigDialog(show: Boolean) {
        _uiState.update { it.copy(showProfileConfigDialog = show) }
    }

    fun onSaveProfile(name: String, email: String, farm: String, photoUrl: String = "") {
        viewModelScope.launch {
            userPreferencesRepository.updateUserName(name)
            userPreferencesRepository.updateUserEmail(email)
            userPreferencesRepository.updateFarmName(farm)
            userPreferencesRepository.updateUserPhotoUrl(photoUrl)
            _uiState.update {
                it.copy(
                    userName = name,
                    userEmail = email,
                    userPhotoUrl = photoUrl,
                    farmName = farm,
                    showProfileConfigDialog = false,
                    toastMessage = "Perfil atualizado com sucesso"
                )
            }
        }
    }

    fun onShowRealtimeVoiceModal(show: Boolean) {
        _uiState.update { it.copy(showRealtimeVoiceModal = show) }
    }

    fun onSelectAttachment(attachment: com.agriia.app.feature.chat.domain.model.ChatAttachment) {
        _uiState.update {
            it.copy(
                pendingAttachment = attachment,
                showAttachmentSheet = false,
                toastMessage = "Anexo adicionado: ${attachment.title}"
            )
        }
    }

    fun onClearAttachment() {
        _uiState.update { it.copy(pendingAttachment = null) }
    }

    fun onSelectModel(modelName: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentModel(modelName)
            _uiState.update {
                it.copy(
                    currentModelName = modelName,
                    showModelSelectorDialog = false
                )
            }
        }
    }

    fun onSendMessage(text: String) {
        val trimmed = text.trim()
        val currentAttachment = _uiState.value.pendingAttachment
        if ((trimmed.isEmpty() && currentAttachment == null) || _uiState.value.isStreaming) return

        val messageText = if (trimmed.isEmpty() && currentAttachment != null) {
            "Anexo: ${currentAttachment.title}"
        } else trimmed

        _uiState.update { it.copy(inputText = "", pendingAttachment = null) }

        viewModelScope.launch {
            val isTemp = _uiState.value.isTemporaryChat
            var conversationId = _uiState.value.currentConversationId

            if (conversationId == null && !isTemp) {
                // Cria nova conversa no Room com título extraído das primeiras palavras
                val title = if (messageText.length > 36) messageText.take(36) + "..." else messageText
                val newConv = chatRepository.createConversation(
                    title = title,
                    modelName = _uiState.value.currentModelName
                )
                conversationId = newConv.id
                _uiState.update { it.copy(currentConversationId = conversationId) }
                observeMessages(conversationId)
            }

            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId ?: "temp_${System.currentTimeMillis()}",
                role = ChatRole.USER,
                content = messageText,
                timestamp = System.currentTimeMillis(),
                attachmentType = currentAttachment?.type?.name,
                attachmentTitle = currentAttachment?.title
            )

            if (!isTemp && conversationId != null) {
                chatRepository.saveMessage(userMessage)
            } else {
                _uiState.update {
                    it.copy(messages = it.messages + userMessage)
                }
            }

            // Inicia resposta streaming da IA
            startAiStreaming(prompt = trimmed, convId = conversationId ?: userMessage.conversationId, isTemp = isTemp)
        }
    }

    private fun startAiStreaming(prompt: String, convId: String, isTemp: Boolean) {
        streamingJob?.cancel()
        val assistantMessageId = UUID.randomUUID().toString()
        val initialAssistantMessage = ChatMessage(
            id = assistantMessageId,
            conversationId = convId,
            role = ChatRole.ASSISTANT,
            content = "",
            timestamp = System.currentTimeMillis(),
            isStreaming = true
        )

        if (isTemp) {
            _uiState.update {
                it.copy(
                    isStreaming = true,
                    messages = it.messages + initialAssistantMessage
                )
            }
        } else {
            _uiState.update { it.copy(isStreaming = true) }
        }

        streamingJob = viewModelScope.launch {
            val accumulated = StringBuilder()
            chatRepository.streamAiResponse(prompt, convId).collect { chunk ->
                accumulated.append(chunk)
                if (isTemp) {
                    _uiState.update { state ->
                        state.copy(
                            messages = state.messages.map { msg ->
                                if (msg.id == assistantMessageId) {
                                    msg.copy(content = accumulated.toString(), isStreaming = true)
                                } else msg
                            }
                        )
                    }
                } else {
                    val currentMsg = initialAssistantMessage.copy(
                        content = accumulated.toString(),
                        isStreaming = true
                    )
                    chatRepository.saveMessage(currentMsg)
                }
            }

            // Finaliza streaming
            if (!isTemp) {
                val finalMsg = initialAssistantMessage.copy(
                    content = accumulated.toString(),
                    isStreaming = false
                )
                chatRepository.saveMessage(finalMsg)
            } else {
                _uiState.update { state ->
                    state.copy(
                        messages = state.messages.map { msg ->
                            if (msg.id == assistantMessageId) {
                                msg.copy(isStreaming = false)
                            } else msg
                        }
                    )
                }
            }
            _uiState.update { it.copy(isStreaming = false) }
        }
    }

    private fun observeMessages(conversationId: String) {
        messagesJob?.cancel()
        messagesJob = viewModelScope.launch {
            chatRepository.getMessagesForConversation(conversationId).collect { list ->
                _uiState.update { it.copy(messages = list) }
            }
        }
    }
}
