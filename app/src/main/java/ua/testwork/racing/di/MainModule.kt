package ua.testwork.racing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.testwork.racing.domain.usecase.LoadRacersUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideLoadRacersUseCase(): LoadRacersUseCase = LoadRacersUseCase()

}