package com.example.room.mvvm

import android.app.Application
import com.example.room.mvvm.model.AppModule
import com.example.room.mvvm.repository.ApiClientModule
import com.example.room.mvvm.repository.ApiComponent
import com.example.room.mvvm.repository.DaggerApiComponent


class MyApplication : Application() {
    private var mApiComponent: ApiComponent? = null

    override fun onCreate() {
        super.onCreate()
        mApiComponent = DaggerApiComponent.builder()
            .appModule(AppModule(this))
            .apiClientModule(ApiClientModule("http://private-222d3-homework5.apiary-mock.com/api/"))
            .build()
    }

    fun getComponent(): ApiComponent? {
        return mApiComponent
    }
}