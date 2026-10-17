package com.agriia.app.feature.chat.data

import com.agriia.app.core.database.dao.ConversationDao
import com.agriia.app.core.database.dao.MessageDao
import com.agriia.app.core.database.entity.ConversationEntity
import com.agriia.app.core.database.entity.MessageEntity
import com.agriia.app.feature.chat.domain.model.ChatMessage
import com.agriia.app.feature.chat.domain.model.ChatRole
import com.agriia.app.feature.chat.domain.model.Conversation
import com.agriia.app.feature.chat.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ChatRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) : ChatRepository {

    override fun getRecentConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMessagesForConversation(conversationId: String): Flow<List<ChatMessage>> {
        return messageDao.getMessagesForConversation(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getConversationById(id: String): Conversation? {
        return conversationDao.getConversationById(id)?.toDomain()
    }

    override suspend fun createConversation(title: String, modelName: String): Conversation {
        val now = System.currentTimeMillis()
        val entity = ConversationEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            createdAt = now,
            updatedAt = now,
            modelName = modelName
        )
        conversationDao.insertConversation(entity)
        return entity.toDomain()
    }

    override suspend fun saveMessage(message: ChatMessage) {
        val entity = MessageEntity(
            id = message.id,
            conversationId = message.conversationId,
            role = when (message.role) {
                ChatRole.USER -> "user"
                ChatRole.ASSISTANT -> "assistant"
            },
            content = message.content,
            timestamp = message.timestamp,
            isStreaming = message.isStreaming,
            isError = message.isError
        )
        messageDao.insertMessage(entity)

        // Atualiza timestamp da conversa
        conversationDao.getConversationById(message.conversationId)?.let { conv ->
            conversationDao.updateConversation(conv.copy(updatedAt = message.timestamp))
        }
    }

    override suspend fun deleteConversation(id: String) {
        conversationDao.deleteById(id)
    }

    override suspend fun seedInitialMockConversationsIfEmpty() {
        // Remove quaisquer conversas mockadas pré-existentes para garantir que apenas
        // interações e conversas reais do usuário sejam exibidas
        conversationDao.deleteMockConversations()
    }

    override fun streamAiResponse(prompt: String, conversationId: String): Flow<String> = flow {
        val simulatedText = buildString {
            append("Olá! Recebi sua mensagem sobre \"")
            append(prompt.take(40))
            if (prompt.length > 40) append("...")
            append("\".\n\nComo modelo agriia de pensamento analítico, aqui está uma análise estruturada para você:\n\n")
            append("1. **Visão Geral**: Abordagem focada em simplicidade, eficiência e organização.\n")
            append("2. **Sugestão Prática**: Recomendamos dividir o problema em etapas iterativas para manter a consistência e clareza.\n")
            append("3. **Próximos Passos**: Posso gerar um rascunho completo, resumo ou checklist prático se desejar.")
        }

        // Simulação fluida de digitação/streaming palavra por palavra
        val words = simulatedText.split(" ")
        for (word in words) {
            emit("$word ")
            delay(40L)
        }
    }

    private fun ConversationEntity.toDomain() = Conversation(
        id = id,
        title = title,
        createdAt = createdAt,
        updatedAt = updatedAt,
        modelName = modelName,
        isPinned = isPinned
    )

    private fun MessageEntity.toDomain() = ChatMessage(
        id = id,
        conversationId = conversationId,
        role = if (role == "user") ChatRole.USER else ChatRole.ASSISTANT,
        content = content,
        timestamp = timestamp,
        isStreaming = isStreaming,
        isError = isError
    )
}
