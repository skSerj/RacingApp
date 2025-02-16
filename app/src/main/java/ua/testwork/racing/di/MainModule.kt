package ua.testwork.racing.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.testwork.racing.data.local.dao.RacersDao
import ua.testwork.racing.data.local.db.AppDatabase
import ua.testwork.racing.data.repository.RacersRepositoryImpl
import ua.testwork.racing.domain.repository.RacersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "racing.db"
        ).build()
    }

    @Provides
    fun provideRacersDao(database: AppDatabase): RacersDao = database.userDao()

    @Provides
    @Singleton
    fun provideRacersRepository(dao: RacersDao): RacersRepository {
        return RacersRepositoryImpl(dao)
    }

}