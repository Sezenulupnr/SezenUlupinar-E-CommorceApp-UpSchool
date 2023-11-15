package com.example.s_shopping.di

import android.content.Context
import androidx.room.Room
import com.example.s_shopping.data.source.local.ProductRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProductRoomDB::class.java, "products_room").build()

    @Singleton
    @Provides
    fun provideDao(roomDB: ProductRoomDB) = roomDB.productDao()

}