package ua.testwork.racing.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity
import ua.testwork.racing.data.local.entity.HistoryShootPracticeEntity.Companion.TABLE_NAME
import ua.testwork.racing.data.local.entity.RacerEntity

@Dao
interface HistoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryShootPracticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(histories: List<HistoryShootPracticeEntity>)

    @Query("SELECT * FROM $TABLE_NAME WHERE internal_id = :id LIMIT 1")
    suspend fun getHistoryByInternalId(id: String): HistoryShootPracticeEntity

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getAllHistories(): List<HistoryShootPracticeEntity>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME")
    fun observeCount(): Flow<Int>


    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

}