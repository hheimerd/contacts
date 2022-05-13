package com.hheimerd.hangouts.di

import android.content.Context
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(modules = [AppModule::class])
interface AppComponent  {
    @Component.Builder
    interface Builder {
        fun setContext(context: Context): Builder
    }
}

@Module
abstract class AppModule {
    // TODO: Add contact repository
}