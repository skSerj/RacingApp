package ua.testwork.racing.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ua.testwork.racing.domain.model.FreeHistoryDomain
import ua.testwork.racing.domain.model.VideoItem
import java.lang.reflect.Type

class ShootHistoryTypeConverter {

    private val gson = GsonBuilder()
        .serializeNulls()
        .serializeSpecialFloatingPointValues()
        .create()

    // VideoItem
    @TypeConverter
    fun stringToVideoItem(data: String?): VideoItem? {
        if (data == null) {
            return null
        }
        val type: Type = object : TypeToken<VideoItem?>() {}.type
        return gson.fromJson<VideoItem>(data, type)
    }

    @TypeConverter
    fun videoItemToString(item: VideoItem?): String? {
        return gson.toJson(item)
    }

    // FreeHistoryDomain
    @TypeConverter
    fun stringToFreeHistoryDomain(data: String?): FreeHistoryDomain? {
        if (data == null) {
            return null
        }
        val type: Type = object : TypeToken<FreeHistoryDomain?>() {}.type
        return gson.fromJson<FreeHistoryDomain>(data, type)
    }

    @TypeConverter
    fun freeHistoryDomainToString(item: FreeHistoryDomain?): String? {
        return gson.toJson(item)
    }

}