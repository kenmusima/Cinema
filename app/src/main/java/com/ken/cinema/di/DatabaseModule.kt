package com.ken.cinema.di

import android.content.Context
import androidx.room.Room
import com.ken.cinema.data.db.AppDatabase
import com.ken.cinema.data.db.dao.TicketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(context,AppDatabase::class.java,"cinema_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesDao(appDatabase: AppDatabase) : TicketDao =
        appDatabase.ticketDao()
}