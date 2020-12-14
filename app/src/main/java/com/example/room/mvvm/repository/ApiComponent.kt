package com.example.room.mvvm.repository

import com.example.room.mvvm.model.AppModule
import com.example.room.mvvm.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ ApiClientModule::class, AppModule::class ])
interface ApiComponent {
    fun inject(mainActivity: MainActivity?)
}