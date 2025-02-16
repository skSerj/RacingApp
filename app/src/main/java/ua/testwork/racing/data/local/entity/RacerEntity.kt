package ua.testwork.racing.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racers")
data class RacerEntity(
    @PrimaryKey(autoGenerate = true)
    val racerId: Int = 0,
    val name: String,
    val numOfWin: Int
)
