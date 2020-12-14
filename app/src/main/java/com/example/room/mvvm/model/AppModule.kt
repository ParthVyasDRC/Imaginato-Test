package com.example.room.mvvm.model

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(mApplication: Application?) {

    private var mApplication: Application? = null

    init {
        this.mApplication = mApplication
    }

    @Provides
    @Singleton
    fun getApplication(): Application? {
        return mApplication
    }
}