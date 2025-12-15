package ua.testwork.racing.data.local.db.converters

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import ua.testwork.racing.domain.model.FreeShotModelDomain
import ua.testwork.racing.domain.model.HistoryTargetModel
import ua.testwork.racing.domain.model.Hole
import ua.testwork.racing.domain.model.Parapet
import ua.testwork.racing.domain.model.PenaltyShape
import ua.testwork.racing.domain.model.PenaltyTarget
import ua.testwork.racing.domain.model.RelativeElement
import ua.testwork.racing.domain.model.ShootingPoint
import ua.testwork.racing.domain.model.ShootingSetDomain
import ua.testwork.racing.domain.model.VideoElementModel
import java.lang.reflect.Type


class ListTypeConverter {

    private var gson = GsonBuilder()
        .serializeSpecialFloatingPointValues()
        .create()

    // Free history

    @TypeConverter
    fun stringToFreeShotList(data: String?): List<FreeShotModelDomain> {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<FreeShotModelDomain>>() {}.type
        return gson.fromJson<List<FreeShotModelDomain>>(data, listType)
    }

    @TypeConverter
    fun freeShotListToString(list: List<FreeShotModelDomain>): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToSetsList(data: String?): List<ShootingSetDomain> {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<ShootingSetDomain>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun shootingSetsToString(list: List<ShootingSetDomain>): String? {
        return gson.toJson(list)
    }


    // TargetEntity

    @TypeConverter
    fun stringToTargetList(data: String?): List<Target>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<Target>?>() {}.type
        return gson.fromJson<List<Target>>(data, listType)
    }

    @TypeConverter
    fun targetListToString(targetsList: List<Target?>?): String? {
        return gson.toJson(targetsList)
    }

    // ParapetEntity

    @TypeConverter
    fun stringToParapetList(data: String?): List<Parapet>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<Parapet>?>() {}.type
        return gson.fromJson<List<Parapet>>(data, listType)
    }

    @TypeConverter
    fun parapetListToString(parapetsList: List<Parapet?>?): String? {
        return gson.toJson(parapetsList)
    }

    // ShootingPointEntity

    @TypeConverter
    fun stringToShootingPointList(data: String?): List<ShootingPoint>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<ShootingPoint>?>() {}.type
        return gson.fromJson<List<ShootingPoint>>(data, listType)
    }

    @TypeConverter
    fun shootingPointListToString(shootingPointList: List<ShootingPoint?>?): String? {
        return gson.toJson(shootingPointList)
    }

    // PenaltyTargetEntity

    @TypeConverter
    fun stringToPenaltyTargetList(data: String?): List<PenaltyTarget>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<PenaltyTarget>?>() {}.type
        return gson.fromJson<List<PenaltyTarget>>(data, listType)
    }

    @TypeConverter
    fun penaltyTargetListToString(penaltyTargetList: List<PenaltyTarget?>?): String? {
        return gson.toJson(penaltyTargetList)
    }

    // PenaltyShapeEntity

    @TypeConverter
    fun penaltyShapeListToString(penaltyShapeList: List<PenaltyShape?>?): String? {
        return gson.toJson(penaltyShapeList)
    }

    @TypeConverter
    fun stringToPenaltyShapeList(data: String?): List<PenaltyShape>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<PenaltyShape>?>() {}.type
        return gson.fromJson<List<PenaltyShape>>(data, listType)
    }

    // PairList

    @TypeConverter
    fun fromPairList(list: List<Pair<Int, Int>>): String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun toPairList(string: String): List<Pair<Int, Int>>? {
        val listType: Type = object : TypeToken<List<Pair<Int, Int>>>() {}.type
        return gson.fromJson<List<Pair<Int, Int>>>(string, listType)
    }

    // HoleEntity

    @TypeConverter
    fun stringToHoleList(data: String?): List<Hole>? {
        if (data == null) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<Hole>?>() {}.type
        return gson.fromJson<List<Hole>>(data, listType)
    }

    @TypeConverter
    fun holeListToString(holesList: List<Hole?>?): String? {
        return gson.toJson(holesList)
    }

    // ArrayList<HistoryTargetModel>

    @TypeConverter
    fun stringToHistoryTargetModelList(data: String?): ArrayList<HistoryTargetModel>? {
        if (data.isNullOrEmpty()) return null

        return try {
            val jsonArray = JsonParser.parseString(data).asJsonArray
            val list = arrayListOf<HistoryTargetModel>()

            for (jsonElement in jsonArray) {
                try {
                    val model = gson.fromJson(jsonElement, HistoryTargetModel::class.java)
                    list.add(model)
                } catch (e: JsonSyntaxException) {
                }
            }
            list
        } catch (e: Exception) {
            null
        }
    }


    @TypeConverter
    fun historyTargetModelListToString(item: ArrayList<HistoryTargetModel>?): String? {
        return gson.toJson(item)
    }

    // RelativeElement
    @TypeConverter
    fun stringToRelativeElementList(data: String?): ArrayList<RelativeElement>? {
        if (data == null) {
            return null
        }
        val type: Type = object : TypeToken<ArrayList<RelativeElement>?>() {}.type
        return gson.fromJson<ArrayList<RelativeElement>>(data, type)
    }

    @TypeConverter
    fun relativeElementListToString(item: ArrayList<RelativeElement>?): String? {
        return gson.toJson(item)
    }

    // RelativeElement
    @TypeConverter
    fun stringToVideoElementModelList(data: String?): ArrayList<VideoElementModel>? {
        if (data == null) {
            return null
        }
        val type: Type = object : TypeToken<ArrayList<VideoElementModel>?>() {}.type
        return gson.fromJson<ArrayList<VideoElementModel>>(data, type)
    }

    @TypeConverter
    fun videoElementModelListToString(item: ArrayList<VideoElementModel>?): String? {
        return gson.toJson(item)
    }

}