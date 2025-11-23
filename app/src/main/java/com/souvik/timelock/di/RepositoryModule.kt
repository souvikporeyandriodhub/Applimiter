package com.souvik.timelock.di

import com.souvik.timelock.data.repository.AppLimitsRepository
import com.souvik.timelock.data.repository.FakeAppLimitsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppLimitsRepository(
        impl: FakeAppLimitsRepository
    ): AppLimitsRepository
}
