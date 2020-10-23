package com.mx.cosmo.orm.vo

import android.os.Parcel
import android.os.Parcelable
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@Suppress("unused")
@DatabaseTable(tableName = SaintInfo.TABLE_NAME)
class SaintInfo() : Parcelable {

    companion object{
        const val TABLE_NAME = "saint_info"
        const val ID = "_id"
        const val COLUMN_IMAGE_SMALL_ID = "image_small_id"
        const val COLUMN_IMAGE_FULL_ID = "image_full_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_UNIT_ID = "unit_id" // 10027701
        const val COLUMN_TYPE = "type"
        const val COLUMN_LANE = "lane"
        const val COLUMN_ACTIVE_TIME = "active_time"
        const val COLUMN_TIERS_PVP = "tiers_PVP"
        const val COLUMN_TIERS_CRUSADE = "tiers_crusade"
        const val COLUMN_TIERS_PVE = "tiers_PVE"
        const val COLUMN_POWER = "power"
        const val COLUMN_RATE_VITALITY = "grow_rate_vitality"
        const val COLUMN_RATE_AURA = "grow_rate_aura"
        const val COLUMN_RATE_TECH = "grow_rate_tech"
        const val COLUMN_VITALITY = "vitality"
        const val COLUMN_AURA = "aura"
        const val COLUMN_TECH = "technique"
        const val COLUMN_HP = "hp"
        const val COLUMN_PHYS_ATTACK = "phys_attack"
        const val COLUMN_FURY_ATTACK = "fury_attack"

        const val COLUMN_PHYS_DEFENSE = "phys_defense"
        const val COLUMN_FURY_RESISTANCE = "fury_resistance"
        const val COLUMN_ACCURACY = "accuracy"
        const val COLUMN_EVASION = "evasion"
        const val COLUMN_HP_RECOVERY = "hp_recovery"
        const val COLUMN_COSMO_RECOVERY = "cosmo_recovery"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CLOTH = "cloth"

        const val COLUMN_IMAGE_SAMLL = "image_small"
        const val COLUMN_IMAGE_FULL = "image_full"

        @JvmField
        val CREATOR = object : Parcelable.Creator<SaintInfo> {
            override fun createFromParcel(parcel: Parcel): SaintInfo {
                return SaintInfo(parcel)
            }

            override fun newArray(size: Int): Array<SaintInfo?> {
                return arrayOfNulls(size)
            }
        }
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

    @DatabaseField
    var name:String = ""

    @DatabaseField(columnName = COLUMN_UNIT_ID)
    var unitId:Int = 0

    @DatabaseField
    var type:String = ""

    @DatabaseField
    var lane:String = ""

    @DatabaseField
    var cloth:String = ""

    @DatabaseField
    var description:String = ""

    @DatabaseField(columnName = COLUMN_ACTIVE_TIME)
    var activeTime:String = ""

    @DatabaseField(columnName = COLUMN_IMAGE_SMALL_ID)
    var imageSmallId:Int = 0

    @DatabaseField(columnName = COLUMN_IMAGE_FULL_ID)
    var imageFullId:Int = 0

    var imageSmall:ByteArray? = byteArrayOf()

    var imageFull:ByteArray? = byteArrayOf()

    var detailInfo:SaintHistory = SaintHistory()

    var detailTier:TierInfo = TierInfo()

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        imageSmallId = parcel.readInt()
        imageFullId = parcel.readInt()
        name = parcel.readString()!!
        unitId = parcel.readInt()
        type = parcel.readString()!!
        lane = parcel.readString()!!
        activeTime = parcel.readString()!!
        cloth = parcel.readString()!!
        description = parcel.readString()!!

        parcel.readByteArray(imageSmall)
        parcel.readByteArray(imageFull)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(imageSmallId)
        parcel.writeInt(imageFullId)
        parcel.writeString(name)
        parcel.writeInt(unitId)
        parcel.writeString(type)
        parcel.writeString(lane)
        parcel.writeString(activeTime)
        parcel.writeString(cloth)
        parcel.writeString(description)

        parcel.writeByteArray(imageSmall)
        parcel.writeByteArray(imageFull)
    }

    override fun describeContents(): Int {
        return 0
    }

}