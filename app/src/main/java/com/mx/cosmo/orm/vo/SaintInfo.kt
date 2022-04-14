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
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CLOTH = "cloth"

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