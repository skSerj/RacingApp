package ua.testwork.racing.domain.model

import android.graphics.Point
import android.graphics.PointF
import android.os.Parcel
import android.os.Parcelable
import androidx.room.TypeConverter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ua.testwork.racing.data.local.db.converters.DateSerializer
import java.util.Date
import java.util.UUID
import kotlin.collections.sortedBy
import kotlin.math.pow
import kotlin.math.sqrt

interface History

@Serializable
data class FreeHistoryDomain(
    val id: Int? = null,
    @Transient
    val dbId: Long? = null,
    val userId: Int? = null,
    val shootPracticsID: String = "",
    val objectType: String? = null,
    val freeShootPracticsShootingTimerID: String? = null,
    val timerSessionID: String? = null,
    val freeShootPracticsTime: Long? = null,
    val freeShootPracticsBestSplit: Long? = null,
    val freeShootPracticsDate: Double = 0.0,
    val gunType: GunType = GunType.HANDGUN_DOT22_LR,
    val freeShootModels: List<FreeShotModelDomain>? = arrayListOf(),
    var sets: List<ShootingSetDomain>? = arrayListOf(),
    val setsCount: Int? = null,
    val shootCount: Int = 0,
    var video: VideoItem? = null,
    @SerializedName("video_id")
    val videoId: Long? = null,
    val timerType: Int = 1,
    val isWithPulse: Boolean = false,
    val isConnectionWasLost: Boolean = false
) : History

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
data class ShootingSetDomain(
    @SerializedName("shot_limit") @SerialName("shot_limit")
    var shotLimit: Int,
    @SerializedName("time_limit") @SerialName("time_limit")
    var timeLimit: Double,
    @SerializedName("shot_count") @SerialName("shot_count")
    var shotCount: Int,
    @SerializedName("best_split") @SerialName("best_split")
    var bestSplit: Double,
    @SerializedName("shots") @SerialName("shots")
    var shots: List<FreeShotModelDomain> = arrayListOf(),
    var time: Double = 0.0,
    var averageTimeBetweenShots: Double = 0.0,
) {
    companion object {
        fun from(exerciseSet: ExerciseHistorySetModel): ShootingSetDomain {
            val result = ShootingSetDomain(
                shotLimit = 0,
                timeLimit = 0.0,
                shotCount = 0,
                bestSplit = 0.0,
                shots = arrayListOf()
            )
            result.shotLimit = exerciseSet.shotLimit
            result.timeLimit = exerciseSet.timeLimit
            result.time = exerciseSet.time
            result.averageTimeBetweenShots = exerciseSet.averageTimeBetweenShots
            result.shotCount = exerciseSet.shots.size
            result.bestSplit = 0.0
            val mappedShots = arrayListOf<FreeShotModelDomain>()
            val sortedShots = exerciseSet.shots.sortedBy { it.time }
            for (i in sortedShots.indices) {
                var split = 0.0
                if (i > 0) {
                    split =
                        sortedShots[i].time - sortedShots[i - 1].time
                    if (result.bestSplit == 0.0 || result.bestSplit > split) {
                        result.bestSplit = split
                    }
                }
                val shot = FreeShotModelDomain(
                    number = i + 1,
                    splitTime = split,
                    time = sortedShots[i].time,
                    pulse = sortedShots[i].pulse,
                    recordedPulseSnapshots = sortedShots[i].recordedPulseSnapshots
                )
                mappedShots.add(shot)
            }
            result.shots = mappedShots
            return result
        }
    }

    fun getSetDuration() = shots.maxOfOrNull { shot -> shot.time } ?: 0.0
}

@Serializable
data class FreeShotModelDomain(
    val id: Int? = null,
    val number: Int,
    var splitTime: Double,
    val time: Double,
    @SerialName("pulse_snapshot")
    var pulse: PulseSnapshot? = null,
    @SerialName("recorded_pulse_snapshots")
    var recordedPulseSnapshots: List<PulseSnapshot>? = null
)

enum class GunType(val type: Int) {
    @SerializedName("255")
    ANY(255),

    @SerializedName("3")
    LESS_LETHAL(3),

    @SerializedName("0")
    AIRSOFT_6MM(0),

    @SerializedName("1")
    HANDGUN_DOT22_LR(1),
}

@Serializable
@Parcelize
data class VideoItem(
    @Expose @SerializedName("id") @SerialName("id")
    var videoId: Long? = null,
    @Expose @SerializedName("file") @SerialName("file")
    var videoLink: String = "",
    @Expose @SerializedName("preview") @SerialName("preview")
    var imageLink: String = "",
    @Expose @SerializedName("video_url") @SerialName("video_url")
    var videoLinkUrl: String = "",
    @Expose @SerializedName("preview_url") @SerialName("preview_url")
    var imageLinkUrl: String = "",
    @Expose @SerializedName("duration") @SerialName("duration")
    var duration: Int = 0,
    @Transient @Expose @SerializedName("file_type") @SerialName("file_type")
    var file_type: Int = 0,
    //був @Transient прибрав для нового videoPlayer, треба протестувати з сервером
    val historyDbId: Int = -1,
    val practiceDate: Long = 0,
    val practiceType: Int = 1,
    var payload: String = "",
    var payloadFileName: String = "",
    @SerializedName("del_private") @SerialName("del_private")
    @Serializable(with = DateSerializer::class)
    var removalDate: Date? = null,
    var isRemovedOnServer: Boolean = false,
    var localPath: String = "",
    var localName: String = "",
    var localImagePath: String? = null,
    var isOwnVideo: Boolean = true,
    var isRemovedLocally: Boolean = false,
    var isCopy: Boolean = false
) : Parcelable

@Parcelize
data class ExerciseHistorySetModel(
    val id: Int = 0,
    val time: Double = 0.0,
    val averageTimeBetweenShots: Double = 0.0,
    val pause: Double = 0.0,
    val timeLimit: Double = 0.0,
    val shotLimit: Int = 0,
    val shots: List<ExerciseShootModel> = listOf(),
) : Parcelable {
    fun mapToShootingSet(): ShootingSetDomain {
        return ShootingSetDomain.from(this)
    }
}

@Parcelize
data class ExerciseShootModel(
    var id: Int? = null,
    var time: Double,
    var delay: Double = 0.0,
    val pulse: @RawValue PulseSnapshot? = null,
    val recordedPulseSnapshots: @RawValue List<PulseSnapshot>? = null
) : Parcelable

@Parcelize
@Serializable
data class PulseSnapshot(
    @Transient
    val id: String = UUID.randomUUID().toString(),
    @SerialName("roll") var roll: Double = 0.0,
    @SerialName("pitch") var pitch: Double = 0.0,
    @SerialName("yaw") var yaw: Double = 0.0,
    @SerialName("stability") var stability: Double = 0.0,
    @SerialName("is_shot") var isShoot: Boolean = false,
    @SerialName("stability_point") var stabilityPoint: List<Double?> = listOf(0.0, 0.0)
) : Parcelable {

    constructor(
        roll: Double,
        pitch: Double,
        yaw: Double,
        stability: Double,
        stabilityPoint: DoubleArray,
        isShoot: Boolean
    ) : this() {
        this.roll = if (roll.isNaN()) 0.0 else roll
        this.pitch = if (pitch.isNaN()) 0.0 else pitch
        this.yaw = if (yaw.isNaN()) 0.0 else yaw
        this.stability = if (stability.isNaN()) 0.0 else stability
        this.isShoot = isShoot

        if (stabilityPoint[0].isNaN() || stabilityPoint[1].isNaN()) {
            this.stabilityPoint = listOf(0.0, 0.0)
        } else {
            this.stabilityPoint = stabilityPoint.toList()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PulseSnapshot

        if (roll != other.roll) return false
        if (pitch != other.pitch) return false
        if (yaw != other.yaw) return false
        if (stability != other.stability) return false
        if (isShoot != other.isShoot) return false
        if (stabilityPoint != other.stabilityPoint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roll.hashCode()
        result = 31 * result + pitch.hashCode()
        result = 31 * result + yaw.hashCode()
        result = 31 * result + stability.hashCode()
        result = 31 * result + isShoot.hashCode()
        result = 31 * result + stabilityPoint.hashCode()
        return result
    }

    override fun toString(): String {
        return "PulseSnapshot(roll=$roll, pitch=$pitch, yaw=$yaw, stability=$stability, isShoot=$isShoot, stabilityPoint=$stabilityPoint)"
    }

    override fun describeContents(): Int = 0

    companion object : Parceler<PulseSnapshot> {

        override fun create(parcel: Parcel): PulseSnapshot {
            val id = parcel.readString() ?: UUID.randomUUID().toString()
            val roll = parcel.readDouble()
            val pitch = parcel.readDouble()
            val yaw = parcel.readDouble()
            val stability = parcel.readDouble()
            val isShoot = parcel.readByte() != 0.toByte()

            val stabilityPointList = mutableListOf<Double?>()
            parcel.readList(stabilityPointList, Double::class.java.classLoader)

            return PulseSnapshot(
                id = id,
                roll = roll,
                pitch = pitch,
                yaw = yaw,
                stability = stability,
                isShoot = isShoot,
                stabilityPoint = stabilityPointList
            )
        }

        override fun PulseSnapshot.write(
            parcel: Parcel,
            flags: Int
        ) {
            parcel.writeString(id)
            parcel.writeDouble(roll)
            parcel.writeDouble(pitch)
            parcel.writeDouble(yaw)
            parcel.writeDouble(stability)
            parcel.writeByte(if (isShoot) 1 else 0)
            parcel.writeList(stabilityPoint)
        }
    }
}

@Parcelize
data class HistoryTargetModel(
    @Expose var targetID: String = "",
    @Expose var targetName: String = "",
    @Expose var zoneLimited: Int = 0,
    @Expose var shootLimited: Int = 0,
    @Expose var rangeLimited: Double = 0.0,
    @Expose var targetType: Int = TargetType.STANDARD.type,
    @Expose var targetImage: String = "",
    @Expose var targetEfficiency: Double = 0.0,
    @Expose var zonesArray: ArrayList<ZoneModel> = arrayListOf(),
    /// true = Hit, false = Miss for each penalty target that attached to this target
    @Expose var penaltyHits: ArrayList<Boolean> = arrayListOf(),
    @Expose var gunType: Int = GunType.HANDGUN_DOT22_LR.type,
    @Expose var noShotCount: Int = 0,
    @Expose var npmCount: Int = 0
) : Parcelable {}


@Serializable(with = TargetTypeSerializer::class)
enum class TargetType(val type: Int) {
    @SerializedName("0")
    STANDARD(0),

    @SerializedName("1")
    METRIC(1),

    @SerializedName("2")
    PLATE(2),

    @SerializedName("3")
    POPPER(3);

    fun heightInCentimeters(): Int {
        return when (this) {
            STANDARD -> {
                57
            }

            METRIC -> {
                75
            }

            PLATE -> {
                33
            }

            POPPER -> {
                85
            }
        }
    }

    fun widthInCentimeters(): Int {
        return when (this) {
            STANDARD -> {
                45
            }

            METRIC -> {
                45
            }

            PLATE -> {
                30
            }

            POPPER -> {
                30
            }
        }
    }

    companion object {
        fun getFrom(type: Int): TargetType? {
            return values().firstOrNull { it.type == type }
        }
    }
}

object TargetTypeSerializer : KSerializer<TargetType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TargetType", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: TargetType) {

        encoder.encodeInt(value.type)
    }

    override fun deserialize(decoder: Decoder): TargetType {
        val value = decoder.decodeInt()
        return TargetType.getFrom(value)!!
    }
}

@Parcelize
data class ZoneModel(
    @Expose @SerializedName("zoneName")
    var name: String = "",
    @Expose @SerializedName("zonesShootingCount")
    var shootingCount: Int = 0
) : Parcelable {

    companion object {
        const val ZONE_NAME_ALPHA = "Alpha"
        const val ZONE_NAME_BRAVO = "Bravo"
        const val ZONE_NAME_CHARLEY = "Charley"
        const val ZONE_NAME_DELTA = "Delta"
        const val ZONE_NAME_MISS = "Missed"
        const val ZONE_NAME_NO_SHOOT = "NoShot"
        const val ZONE_NAME_STEEL = "Steel"
        const val ZONE_NAME_MISS_STEEL = "MissedSteel"
        const val ZONE_NAME_NO_SHOOT_STEEL = "NoShotSteel"
        const val ZONE_NAME_NPM = "NPM"
    }
}

@kotlinx.serialization.Serializable(with = ParapetSerializer::class)
class Parapet(
    val id: String = "",
    val parapetType: Int = ParapetType.BREASTWORK.type,
    var startPointX: Int,
    var startPointY: Int,
    var finishPointX: Int,
    var finishPointY: Int,
    var tiresCount: Int,
    var wallHeight: Int,
    // отверстия в стене
    var holes: List<Hole>
)

object ParapetSerializer : KSerializer<Parapet> {
    override val descriptor: SerialDescriptor = ParapetSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Parapet) {
        val surrogate = ParapetSurrogate()
        surrogate.fillFrom(value)
        encoder.encodeSerializableValue(ParapetSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Parapet {
        val surrogate = decoder.decodeSerializableValue(ParapetSurrogate.serializer())
        val holes = try {
            Json.decodeFromString<List<Hole>>(surrogate.holes ?: "[]")
        } catch (e: Exception) {
            listOf<Hole>()
        }
        return Parapet(
            id = UUID.randomUUID().toString(),
            parapetType = surrogate.parapetType ?: 0,
            startPointX = surrogate.startPointX ?: 0,
            startPointY = surrogate.startPointY ?: 0,
            finishPointX = surrogate.finishPointX ?: 0,
            finishPointY = surrogate.finishPointY ?: 0,
            tiresCount = surrogate.tiresCount ?: 0,
            wallHeight = ((surrogate.wallHeight ?: 0.0) * 100.0).toInt(),
            holes = holes
        )
    }
}

@Serializable
private class ParapetSurrogate() {

    var startPointX: Int? = null
    var startPointY: Int? = null
    var finishPointX: Int? = null
    var finishPointY: Int? = null
    var parapetType: Int? = null
    var tiresCount: Int? = null
    var wallHeight: Double? = null
    var holes: String? = null

    fun fillFrom(from: Parapet) {
        startPointX = from.startPointX
        startPointY = from.startPointY
        finishPointX = from.finishPointX
        finishPointY = from.finishPointY
        if (finishPointX == null) {
            finishPointX = startPointX
        }
        if (finishPointY == null) {
            finishPointY = startPointY
        }
        parapetType = from.parapetType
        if (from.parapetType != ParapetType.TIRES.type) {
            wallHeight = from.wallHeight.toDouble() / 100.0
            if (wallHeight!! < 0.25) {
                wallHeight = 2.0
            }
        } else {
            tiresCount = from.tiresCount
        }
        holes = Json.encodeToString(from.holes)
    }
}

enum class ParapetType(val type: Int) {
    @SerializedName("0")
    BREASTWORK(0),
    @SerializedName("1")
    WALL(1),
    @SerializedName("2")
    TIRES(2);

    companion object {
        fun getFrom(type: Int): ParapetType? {
            return ParapetType.values().firstOrNull { it.type == type }
        }
    }
}

@kotlinx.serialization.Serializable
data class Hole(
    val holeType: HoleType,
    val holeShape: HoleShape
)


@Serializable(with = HoleTypeSerializer::class)
enum class HoleType(val type: Int) {
    RECT(0), CIRCLE(1);

    companion object {
        fun getFrom(type: Int): HoleType? {
            return values().firstOrNull { it.type == type }
        }
    }
}

object HoleTypeSerializer : KSerializer<HoleType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("HoleType", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: HoleType) {
        encoder.encodeInt(value.type)
    }

    override fun deserialize(decoder: Decoder): HoleType {
        val value = decoder.decodeInt()
        return HoleType.getFrom(value)!!
    }
}

@kotlinx.serialization.Serializable(with = HoleShapeSerializer::class)
interface HoleShape {
    /// insert point measured in meters from bottom left corner of the wall
    val insertPoint: PointF
}

object HoleShapeSerializer : JsonContentPolymorphicSerializer<HoleShape>(HoleShape::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out HoleShape> {
        return if (element.jsonObject["radius"]?.jsonPrimitive?.contentOrNull != null) {
            CircleShape.serializer()
        } else {
            RectangleShape.serializer()
        }
    }
}

@kotlinx.serialization.Serializable(with = CircleShapeSerializer::class)
data class CircleShape(
    /// insert point measured in meters from bottom left corner of the wall
    override var insertPoint: PointF = PointF(),
    /// center point of circle measured in meters from bottom left corner of the wall
    var centerPoint: PointF = PointF(),
    /// radius of circle measured in meters
    var radius: Double = 0.0
) : HoleShape {

}

@kotlinx.serialization.Serializable(with = RectangleShapeSerializer::class)
data class RectangleShape(
    /// insert point measured in meters from bottom left corner of the wall
    override var insertPoint: PointF = PointF(),
    /// point measured in meters from bottom left corner of the wall
    var bottomLeftCorner: PointF = PointF(),
    /// point measured in meters from bottom left corner of the wall
    var topRightCorner: PointF = PointF()
) : HoleShape {

}

object CircleShapeSerializer : KSerializer<CircleShape> {
    override val descriptor: SerialDescriptor = CircleShapeSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: CircleShape) {
        val surrogate = CircleShapeSurrogate()
        surrogate.fillFrom(value)
        encoder.encodeSerializableValue(CircleShapeSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): CircleShape {
        val surrogate = decoder.decodeSerializableValue(CircleShapeSurrogate.serializer())
        return CircleShape(
            PointF(surrogate.centerPointX ?: 0.0f, surrogate.centerPointY ?: 0.0f),
            PointF(surrogate.centerPointX ?: 0.0f, surrogate.centerPointY ?: 0.0f),
            surrogate.radius ?: 0.0
        )
    }
}

object RectangleShapeSerializer : KSerializer<RectangleShape> {
    override val descriptor: SerialDescriptor = RectangleShapeSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: RectangleShape) {
        val surrogate = RectangleShapeSurrogate()
        surrogate.fillFrom(value)
        encoder.encodeSerializableValue(RectangleShapeSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): RectangleShape {
        val surrogate = decoder.decodeSerializableValue(RectangleShapeSurrogate.serializer())
        return RectangleShape(
            PointF(surrogate.bottomLeftCornerX ?: 0.0f, surrogate.bottomLeftCornerY ?: 0.0f),
            PointF(surrogate.bottomLeftCornerX ?: 0.0f, surrogate.bottomLeftCornerY ?: 0.0f),
            PointF(surrogate.topRightCornerX ?: 0.0f, surrogate.topRightCornerY ?: 0.0f)
        )
    }
}

@Serializable
private class CircleShapeSurrogate() {
    var centerPointX: Float? = null
    var centerPointY: Float? = null
    var radius: Double? = null

    fun fillFrom(from: CircleShape) {
        centerPointX = from.centerPoint.x
        centerPointY = from.centerPoint.y
        radius = from.radius
    }
}

@Serializable
private class RectangleShapeSurrogate() {
    var bottomLeftCornerY: Float? = null
    var topRightCornerX: Float? = null
    var bottomLeftCornerX: Float? = null
    var topRightCornerY: Float? = null


    fun fillFrom(from: RectangleShape) {
        bottomLeftCornerX = from.bottomLeftCorner.x
        bottomLeftCornerY = from.bottomLeftCorner.y
        topRightCornerX = from.topRightCorner.x
        topRightCornerY = from.topRightCorner.y
    }
}

data class VideoElementModel(
    val type: VideoElementType,
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 0f,
    var height: Float = 0f,
    var isOptionEnabled: Boolean = false,
)

enum class VideoElementType(val index: Int) {
    BUTTON_START(0), // case startStopButton = 0
    BUTTON_RECORD(1), // case videoRecButton = 1
    SHOTS_LIST(2), // case shotList = 2
    COUNT_DOWN(3), // case countdown = 3
    RECORD_TIME(4), // case recordingTime = 4
    SHOTS_NUMBER(5), // case shotCount = 5
    TARGET_NUMBER(6), // case targetCount = 6
    BEST_SPLIT(7), // case bestSplit = 7
    MAX_RANGE(8), // case maxRange = 8
    SETS_LIST(9), // case setList = 9
    FIRST_SHOT(10), // case firstShot = 10
    LOGO(11), // case logo = 11
    HORIZONTAL_LEVEL(12), // case horizontal_level = 12
    STABILITY(13), // case stability = 13
    BARREL_ANGLE(14); // case elevation = 14

    companion object  {
        fun from(index: Int): VideoElementType {
            return when (index) {
                1 -> BUTTON_RECORD
                2 -> SHOTS_LIST
                3 -> COUNT_DOWN
                4 -> RECORD_TIME
                5 -> SHOTS_NUMBER
                6 -> TARGET_NUMBER
                7 -> BEST_SPLIT
                8 -> MAX_RANGE
                9 -> SETS_LIST
                10 -> FIRST_SHOT
                11 -> LOGO
                12 -> HORIZONTAL_LEVEL
                13 -> STABILITY
                14 -> BARREL_ANGLE
                else -> BUTTON_START
            }
        }
    }
}

data class RelativeElement(
    var rcX: Float = 0f,
    var rcY: Float = 0f,
    var width: Float = 0f,
    var isVisible: Boolean = false,
    var rTextSize: Float = 0f, // = textSize/width
    var relativeRadius: Float = 0f,
    var rMargin: Float = 0f, //отсутп между входящими элементами
    val rowCount: Int = 5, //число строк, выводимое в списке
    val type: VideoElementType = VideoElementType.FIRST_SHOT,
) {

}

@Serializable(with = PenaltyShapeSerializer::class)
data class PenaltyShape(
    val penaltyID: String,
    var points: List<Point>,
    var isClosed: Boolean,
    var shapeType: Int = PenaltyShapeType.FREE.type
)

object PenaltyShapeSerializer : KSerializer<PenaltyShape> {
    override val descriptor: SerialDescriptor = PenaltyShapeSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: PenaltyShape) {
        val surrogate = PenaltyShapeSurrogate()
        try {
            surrogate.fillFrom(value)
        } catch (e: Exception) {
        }

        encoder.encodeSerializableValue(PenaltyShapeSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): PenaltyShape {
        val surrogate = decoder.decodeSerializableValue(PenaltyShapeSurrogate.serializer())
        return PenaltyShape(
            surrogate.penaltyID ?: "",
            surrogate.points?.map {
                try {
                    Point(it[0], it[1])
                } catch (e: Exception) {
                    Point(0, 0)
                }
            } ?: listOf(),
            surrogate.isClosed ?: false,
            PenaltyShapeType.getFrom(surrogate.shapeType ?: 0)?.type!!
        )
    }
}

enum class PenaltyShapeType(val type: Int)  {
    FREE(0), RECTANGLE(1);

    companion object {
        fun getFrom(type: Int): PenaltyShapeType? {
            return PenaltyShapeType.values().firstOrNull { it.type == type }
        }
    }
}

@Serializable
private class PenaltyShapeSurrogate() {
    var penaltyID: String? = null
    var points: List<List<Int>>? = null
    var isClosed: Boolean? = null
    var shapeType: Int? = null

    fun fillFrom(from: PenaltyShape) {
        penaltyID = from.penaltyID
        points = from.points.map {
            listOf(it.x, it.y)
        }
        isClosed = from.isClosed
        shapeType = from.shapeType
    }
}

@Serializable(with = PenaltyTargetSerializer::class)
data class PenaltyTarget (
    val isAbove: Boolean,
    val angle: Double,
    val isAttachedToMainStand: Boolean,
    val shiftPoint: List<Double>
)

object PenaltyTargetSerializer : KSerializer<PenaltyTarget> {
    override val descriptor: SerialDescriptor = PenaltyTargetSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: PenaltyTarget) {
        val surrogate = PenaltyTargetSurrogate()
        surrogate.fillFrom(value)
        encoder.encodeSerializableValue(PenaltyTargetSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): PenaltyTarget {
        val surrogate = decoder.decodeSerializableValue(PenaltyTargetSurrogate.serializer())
        return PenaltyTarget(
            isAbove = surrogate.isAbove ?: false,
            angle = Math.toDegrees(surrogate.angle ?: 0.0),
            isAttachedToMainStand = surrogate.isAttachedToMainStand ?: false,
            shiftPoint = surrogate.shiftPoint ?: listOf()
        )
    }
}

@Serializable
private class PenaltyTargetSurrogate() {

    var isAbove: Boolean? = null
    var angle: Double? = null
    var isAttachedToMainStand: Boolean? = null
    var shiftPoint: List<Double>? = null

    fun fillFrom(from: PenaltyTarget) {
        isAbove = from.isAbove
        angle = Math.toRadians(from.angle)
        isAttachedToMainStand = from.isAttachedToMainStand
        shiftPoint = from.shiftPoint
    }

}

@kotlinx.serialization.Serializable
data class ShootingPoint (
    @Transient
    var id: String = UUID.randomUUID().toString(),
    var targetID: List<String>? = null,
    var pointX: Int,
    var pointY: Int,
    var height: Double,
    //  у нас теоретически должны еще добавиться точки стрельбы без остановки.
    //  Т.е. человек движется и стреляет в движении. Эта булевская переменная показывает,
    //  должна ли происходить остановка в точке стрельбы. Сейчас пока везде true (1)
    val isShouldStop: Int
): java.io.Serializable {

    init {
        if (targetID == null) {
            targetID = listOf()
        }
    }

    fun distance(to: ShootingPoint): Float {
        var temp = (pointX.toFloat() - to.pointX.toFloat()).pow(2.0f)
        temp += (pointY.toFloat() - to.pointY.toFloat()).pow(2.0f)
        return sqrt(temp)
    }

    override fun equals(other: Any?): Boolean {
        if (other is ShootingPoint) {
            return targetID == other.targetID &&
                    pointX == other.pointX &&
                    pointY == other.pointY &&
                    height == other.height &&
                    isShouldStop == other.isShouldStop
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + targetID.hashCode()
        result = 31 * result + pointX
        result = 31 * result + pointY
        result = 31 * result + height.hashCode()
        result = 31 * result + isShouldStop
        return result
    }

}

enum class StageType(val value: Int) {
    REGULAR(0),
    QUICK(1),
    RO(2),
    EXERCISE(3);

    companion object {
        fun getFrom(value: Int): StageType {
            return when (value) {
                1 -> QUICK
                2 -> RO
                3 -> EXERCISE
                else -> REGULAR
            }
        }
    }
}

class ListStringTypeConverter {

    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(string: String?): List<String> {
        return string?.takeIf { it.isNotBlank() }?.split(",") ?: emptyList()
    }
}

class DateTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?) = date?.time

    @TypeConverter
    fun fromDoubleTimestamp(value: Double?) = value?.let { Date(it.toLong()) }

    @TypeConverter
    fun dateToDoubleTimestamp(date: Date?) = date?.time?.toDouble()
}

