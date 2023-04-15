package com.lablabla.goldenfish.di

import android.app.Application
import androidx.room.Room
import com.lablabla.goldenfish.data.local.GoldenFishDatabase
import com.lablabla.goldenfish.data.model.MQTTConfigMessage
import com.lablabla.goldenfish.data.model.Message
import com.lablabla.goldenfish.data.model.Opcode
import com.lablabla.goldenfish.data.remote.MqttRemoteServiceImpl
import com.lablabla.goldenfish.domain.repository.RemoteService
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(app: Application): GoldenFishDatabase {
        return Room.databaseBuilder(
            app,
            GoldenFishDatabase::class.java,
            "godlen_fish.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesRemoteService(): RemoteService {
        return MqttRemoteServiceImpl()
    }

    @Provides
    @Singleton
    fun providesMoshiForMessages(): JsonAdapter<Message> {
        val moshi = Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Message::class.java, "opcode")
                    .withSubtype(MQTTConfigMessage::class.java, Opcode.MQTT_CONFIG.ordinal.toString())
            )
            .add(KotlinJsonAdapterFactory())
            .build()
        return moshi.adapter(Message::class.java)
    }
}