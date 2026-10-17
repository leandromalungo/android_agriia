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

    override fun searchConversations(query: String): Flow<List<Conversation>> {
        return conversationDao.searchConversations(query).map { entities ->
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
            modelName = modelName,
            lastMessageSnippet = null
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
            isError = message.isError,
            attachmentType = message.attachmentType,
            attachmentTitle = message.attachmentTitle
        )
        messageDao.insertMessage(entity)

        // Atualiza timestamp e resumo da conversa no Room
        val snippet = if (message.content.isNotBlank()) message.content.take(60) else null
        conversationDao.updateLastActivity(
            id = message.conversationId,
            updatedAt = message.timestamp,
            lastSnippet = snippet
        )
    }

    override suspend fun renameConversation(id: String, newTitle: String) {
        conversationDao.updateTitle(id = id, newTitle = newTitle)
    }

    override suspend fun deleteConversation(id: String) {
        conversationDao.deleteById(id)
    }

    override suspend fun seedInitialMockConversationsIfEmpty() {
        // Exclui quaisquer dados mock antigos para preservar apenas as interações reais do usuário
        conversationDao.deleteMockConversations()
    }

    override fun streamAiResponse(prompt: String, conversationId: String): Flow<String> = flow {
        val simulatedText = buildString {
            append("Olá! Analisei sua solicitação sobre \"")
            append(prompt.take(35))
            if (prompt.length > 35) append("...")
            append("\".\n\nComo modelo agriia de raciocínio lógico, aqui estão os pontos-chave:\n\n")
            append("• **Fundamentação**: Abordagem estruturada com persistência local de dados no Room.\n")
            append("• **Próxima Ação**: Você pode continuar essa conversa ou acessar o histórico completo a qualquer momento pelo menu lateral.")
        }

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
        lastMessageSnippet = lastMessageSnippet,
        isPinned = isPinned
    )

    private fun MessageEntity.toDomain() = ChatMessage(
        id = id,
        conversationId = conversationId,
        role = if (role == "user") ChatRole.USER else ChatRole.ASSISTANT,
        content = content,
        timestamp = timestamp,
        isStreaming = isStreaming,
        isError = isError,
        attachmentType = attachmentType,
        attachmentTitle = attachmentTitle
    )
}
