package ua.testwork.racing.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.testwork.racing.data.local.dao.HistoriesDao
import ua.testwork.racing.data.local.dao.RacersDao
import ua.testwork.racing.data.local.db.converters.ListTypeConverter
import ua.testwork.racing.data.local.db.converters.ShootHistoryTypeConverter
import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity
import ua.testwork.racing.data.local.entity.RacerEntity
import ua.testwork.racing.domain.model.DateTypeConverter
import ua.testwork.racing.domain.model.ListStringTypeConverter

@Database(
    entities = [RacerEntity::class, HistoryShootPracticeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListTypeConverter::class,
    ShootHistoryTypeConverter::class,
    ListStringTypeConverter::class,
    DateTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): RacersDao
    abstract fun historiesDao(): HistoriesDao
}