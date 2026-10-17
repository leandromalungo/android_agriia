package com.agriia.app.feature.chat.domain.repository

import com.agriia.app.feature.chat.domain.model.ChatMessage
import com.agriia.app.feature.chat.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getRecentConversations(): Flow<List<Conversation>>
    fun searchConversations(query: String): Flow<List<Conversation>>
    fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>>
    suspend fun getConversationById(id: String): Conversation?
    suspend fun createConversation(title: String, modelName: String): Conversation
    suspend fun saveMessage(message: ChatMessage)
    suspend fun renameConversation(id: String, newTitle: String)
    suspend fun deleteConversation(id: String)
    suspend fun seedInitialMockConversationsIfEmpty()
    fun streamAiResponse(prompt: String, conversationId: String): Flow<String>
}
