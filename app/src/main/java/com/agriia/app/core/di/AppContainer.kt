package com.agriia.app.core.di

import android.content.Context
import com.agriia.app.core.database.AgriiaDatabase
import com.agriia.app.core.datastore.UserPreferencesRepository
import com.agriia.app.feature.chat.data.ChatRepositoryImpl
import com.agriia.app.feature.chat.domain.repository.ChatRepository

interface AppContainer {
    val chatRepository: ChatRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: AgriiaDatabase by lazy {
        AgriiaDatabase.getDatabase(context)
    }

    override val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(
            conversationDao = database.conversationDao(),
            messageDao = database.messageDao()
        )
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }
}
