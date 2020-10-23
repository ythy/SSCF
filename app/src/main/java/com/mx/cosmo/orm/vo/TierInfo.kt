package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@Suppress("unused")
@DatabaseTable(tableName = TierInfo.TABLE_NAME)
class TierInfo {

    companion object{
        const val TABLE_NAME = "tier_info"
        const val ID = "_id"
        const val COLUMN_TIERS_PVP = "tiers_PVP"
        const val COLUMN_TIERS_CRUSADE = "tiers_crusade"
        const val COLUMN_TIERS_PVE = "tiers_PVE"
        const val COLUMN_SAINT_ID = "saint_id" //   10027701
        const val COLUMN_VERSION = "version" // database 2020-10-21
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = Version.ID)
    var id:Int = 0

    @DatabaseField(columnName = SaintHistory.COLUMN_SAINT_ID)
    var saintId:Int = 0

    @DatabaseField(columnName = SaintHistory.COLUMN_VERSION)
    var version:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_PVP)
    var tiersPVP:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_CRUSADE)
    var tiersCrusade:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_PVE)
    var tiersPVE:String = ""


}

