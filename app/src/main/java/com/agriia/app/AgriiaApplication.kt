package com.agriia.app

import android.app.Application
import com.agriia.app.core.di.AppContainer
import com.agriia.app.core.di.DefaultAppContainer

open class AgriiaApplication : Application() {
    lateinit var container: AppContainer
        protected set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
