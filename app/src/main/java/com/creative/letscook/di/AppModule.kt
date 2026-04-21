package com.creative.letscook.di

import com.creative.letscook.data.repo.KitchenRepositoryImpl
import com.creative.letscook.domain.repo.KitchenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindKitchenRepository(
        kitchenRepositoryImpl: KitchenRepositoryImpl
    ): KitchenRepository
}