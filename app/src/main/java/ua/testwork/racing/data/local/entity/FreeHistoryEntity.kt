package ua.testwork.racing.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ua.testwork.racing.data.local.db.converters.ListTypeConverter
import ua.testwork.racing.data.local.db.converters.ShootHistoryTypeConverter
import ua.testwork.racing.domain.model.FreeHistoryDomain
import ua.testwork.racing.domain.model.HistoryTargetModel
import ua.testwork.racing.domain.model.ListStringTypeConverter
import ua.testwork.racing.domain.model.StageType
import ua.testwork.racing.domain.model.VideoItem
import java.util.Date

@Entity(tableName = HistoryShootPracticeEntity.TABLE_NAME)
data class HistoryShootPracticeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "internal_id")
    var internalId: String?,
    var remoteId: Int? = null,
    var shootPracticsID: String? = "",
    var objectType: String? = "",
    var shootPracticsName: String? = "",
    var shootPracticsTimeLimit: Long? = 0L,
    var shootPracticsFirstSignalDelay: Long? = 0L,
    var shootPracticsShootLimit: Int? = 0,
    var shootPracticsBestSplit: Long? = 0L,
    var shootPracticsAvgEfficiency: Int? = 0,
    var shootPracticsDistance: Double? = 0.0,
    var shootPracticsDescription: String? = "",
    var date: Long? = 0L,
    var dayNumber:Int? = 1000,
    var practiceType: Int? = 0,
    @TypeConverters(ShootHistoryTypeConverter::class)
    @SerializedName("video")
    var video: VideoItem? = null,
    var videoLink: String? = null,
    var hitFactor: Double? = 0.0,
    @TypeConverters(ListTypeConverter::class)
    @SerializedName("shoot_practics_targets")
    var shootPracticsTargets: ArrayList<HistoryTargetModel>? = arrayListOf(),
    @TypeConverters(ShootHistoryTypeConverter::class)
    @SerializedName("sg_timer_session")
    var sgTimerSession: FreeHistoryDomain? = null,
    var timerType: Int? = 0,
    @SerializedName("type")
    var stageType: Int? = StageType.REGULAR.value,
    @TypeConverters(ListStringTypeConverter::class)
    @SerializedName("photos")
    var photos: List<String>? = listOf(),

    @SerializedName("with_pulse")
    @ColumnInfo(name = "with_pulse", defaultValue = "0")
    var isWithPulse: Boolean = false,

    @SerializedName("isConnectionWasLost")
    @ColumnInfo(name = "is_connection_was_lost", defaultValue = "0")
    var isConnectionWasLost: Boolean = false

) : SyncObject() {
    companion object {
        const val TABLE_NAME = "histories"
    }
}

open class SyncObject {
    open var deleted: Date? = null
    open var lastServerSync: Date? = null
}
