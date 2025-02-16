package ua.testwork.racing.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.testwork.racing.data.local.entity.RacerEntity

@Dao
interface RacersDao {

    @Query("SELECT COUNT(*) FROM racers")
    suspend fun getRacerCount(): Int

    @Query("SELECT * FROM racers ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomRacers(count: Int): List<RacerEntity>

    @Query("SELECT * FROM racers")
    fun getAllRacers(): Flow<List<RacerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRacers(racers: List<RacerEntity>)

    @Query("UPDATE racers SET numOfWin = numOfWin + 1 WHERE racerId IN (:racerIds)")
    suspend fun incrementScores(racerIds: List<Int>)

}