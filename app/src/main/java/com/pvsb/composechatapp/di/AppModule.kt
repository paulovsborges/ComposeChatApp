package com.pvsb.composechatapp.di

import com.pvsb.composechatapp.datasource.service.http.MessageService
import com.pvsb.composechatapp.datasource.service.http.MessageServiceImpl
import com.pvsb.composechatapp.datasource.service.websocket.ChatSocketService
import com.pvsb.composechatapp.datasource.service.websocket.ChatSocketServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(httpClient: HttpClient): MessageService {
        return MessageServiceImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideWebSocketService(httpClient: HttpClient): ChatSocketService {
        return ChatSocketServiceImpl(httpClient)
    }
}