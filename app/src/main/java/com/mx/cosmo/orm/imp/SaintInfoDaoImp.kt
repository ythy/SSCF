package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.mx.cosmo.orm.vo.ImageInfo
import com.mx.cosmo.orm.vo.SaintHistory
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.TierInfo

class SaintInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SaintInfo, Int>(getDao(orm)) {

    companion object{
        private var dao: Dao<SaintInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SaintInfo, Int> {
            return if(dao == null)
                orm.getDao(SaintInfo::class.java)
            else
                dao!!
        }
    }

    fun createNewSaint(saintInfo: SaintInfo){
        val current = this.queryForEq(SaintInfo.COLUMN_UNIT_ID, saintInfo.unitId)
        if(current == null || current.isEmpty()){
            this.create(saintInfo)
        }
    }

    fun updateSaintSmallImage(id:Int, imageId:Int){
        val ub = updateBuilder()
        ub.updateColumnValue(SaintInfo.COLUMN_IMAGE_SMALL_ID, imageId)
        ub.where().eq(SaintInfo.ID, id)
        ub.update()
    }

    fun updateSaintFullImage(id:Int, imageId:Int){
        val ub = updateBuilder()
        ub.updateColumnValue(SaintInfo.COLUMN_IMAGE_FULL_ID, imageId)
        ub.where().eq(SaintInfo.ID, id)
        ub.update()
    }

    fun getSaintList(orderBy:String, isAsc:Boolean = false):List<SaintInfo>{
        val tableDetail = "detail"
        val tableTier = "tier"
        val tableImage = "image"
        val sql = "SELECT ${SaintInfo.ID}, ${SaintInfo.COLUMN_NAME}, " +
                " ${SaintHistory.COLUMN_RATE_VITALITY}, ${SaintHistory.COLUMN_RATE_AURA}, ${SaintHistory.COLUMN_RATE_TECH}," +
                " ${TierInfo.COLUMN_TIERS_PVP}, ${TierInfo.COLUMN_TIERS_PVE}, ${ImageInfo.COLUMN_IMAGE}, " +
                " ${SaintInfo.COLUMN_UNIT_ID}, ${SaintInfo.COLUMN_ACTIVE_TIME} " +
                "  FROM ${SaintInfo.TABLE_NAME} LEFT JOIN " +

                " ( SELECT MAX(${SaintHistory.COLUMN_VERSION}) DV, ${SaintHistory.COLUMN_SAINT_ID} DI, " +
                " ${SaintHistory.COLUMN_RATE_VITALITY}, ${SaintHistory.COLUMN_RATE_AURA}, ${SaintHistory.COLUMN_RATE_TECH} " +
                " FROM ${SaintHistory.TABLE_NAME} " +
                " GROUP BY DI ) $tableDetail " +
                " ON " +
                "  $tableDetail.DI == ${SaintInfo.TABLE_NAME}.${SaintInfo.COLUMN_UNIT_ID} " +

                " LEFT JOIN " +
                " ( SELECT MAX(${TierInfo.COLUMN_VERSION}) TV, ${TierInfo.COLUMN_SAINT_ID} TI, " +
                " ${TierInfo.COLUMN_TIERS_PVP}, ${TierInfo.COLUMN_TIERS_PVE} " +
                " FROM ${TierInfo.TABLE_NAME} " +
                " GROUP BY TI ) $tableTier " +
                " ON " +
                "  $tableTier.TI == ${SaintInfo.TABLE_NAME}.${SaintInfo.COLUMN_UNIT_ID} " +

                " LEFT JOIN " +
                " ( SELECT ${ImageInfo.COLUMN_IMAGE}, ${ImageInfo.ID} II " +
                " FROM ${ImageInfo.TABLE_NAME} ) $tableImage" +
                " ON " +
                "  $tableImage.II == ${SaintInfo.TABLE_NAME}.${SaintInfo.COLUMN_IMAGE_SMALL_ID} " +

                " ORDER BY $orderBy ${if (isAsc) "ASC" else "DESC"} "

        val rawResults = this.queryRaw(sql,
            arrayOf(DataType.INTEGER, DataType.STRING, DataType.DOUBLE, DataType.DOUBLE, DataType.DOUBLE,
                DataType.STRING, DataType.STRING, DataType.BYTE_ARRAY, DataType.INTEGER, DataType.STRING))
        val result = mutableListOf<SaintInfo>()
        for (resultArray in rawResults) {
            val saintInfo = SaintInfo()
            saintInfo.id = resultArray[0] as Int
            saintInfo.name = resultArray[1].toString()
            saintInfo.detailInfo.vitalityRate = resultArray[2].toString().toDouble()
            saintInfo.detailInfo.auraRate = resultArray[3].toString().toDouble()
            saintInfo.detailInfo.techRate = resultArray[4].toString().toDouble()
            saintInfo.detailTier.tiersPVP = resultArray[5].toString()
            saintInfo.detailTier.tiersPVE = resultArray[6].toString()
            if(resultArray[7] != null)
                saintInfo.imageSmall = resultArray[7] as ByteArray
            saintInfo.unitId = resultArray[8] as Int
            saintInfo.activeTime = resultArray[9].toString()
            result.add(saintInfo)
        }
        return result
    }

}

