package com.souvik.timelock.di

import android.content.Context
import com.souvik.timelock.data.repository.AppListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppListRepository(
        @ApplicationContext context: Context     // 🔥 FIX HERE
    ): AppListRepository {
        return AppListRepository(context)
    }
}
