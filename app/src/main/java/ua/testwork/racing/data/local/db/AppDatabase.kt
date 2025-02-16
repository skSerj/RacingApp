package ua.testwork.racing.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.testwork.racing.data.local.dao.RacersDao
import ua.testwork.racing.data.local.entity.RacerEntity

@Database(entities = [RacerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): RacersDao
}