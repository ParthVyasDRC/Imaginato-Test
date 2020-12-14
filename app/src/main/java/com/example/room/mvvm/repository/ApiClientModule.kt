package com.example.room.mvvm.repository

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApiClientModule(url: String?) {

    var BASE_URL: String? = null

   init {
       BASE_URL = url
   }

    @Provides
    @Singleton
    fun getClient(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}