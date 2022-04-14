package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.j256.ormlite.stmt.Where
import com.mx.cosmo.orm.vo.ImageInfo
import com.mx.cosmo.orm.vo.SkillsHistory

class SkillsHistoryDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SkillsHistory, Int>(getDao(orm)) {

    companion object{
        private var dao: Dao<SkillsHistory, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SkillsHistory, Int> {
            return if(dao == null)
                orm.getDao(SkillsHistory::class.java)
            else
                dao!!
        }
    }

    fun createSkillsDetail(detail: SkillsHistory){
        val qb = this.queryBuilder()
        qb.orderBy(SkillsHistory.ID, false)
        qb.where().eq(SkillsHistory.COLUMN_UNIT_ID, detail.unitId)
        val oldDetail = qb.queryForFirst()
        if(oldDetail == null){
            this.create(detail)
        }else if(!( oldDetail.level == detail.level
                    && oldDetail.name == detail.name
                    && oldDetail.description == detail.description
                    && oldDetail.effects == detail.effects
                   )){
            this.create(detail)
        }
    }

    fun querySkillsHistory(skillsId:Int):List<SkillsHistory>{
        val qb = this.queryBuilder()
        val subSql = " SELECT ${ImageInfo.COLUMN_IMAGE} FROM ${ImageInfo.TABLE_NAME} " +
                " WHERE ${ImageInfo.TABLE_NAME}.${ImageInfo.ID} = ${SkillsHistory.TABLE_NAME}.${SkillsHistory.COLUMN_IMAGE_ID} "
        qb.selectRaw(SkillsHistory.ID + ", " + SkillsHistory.COLUMN_VERSION + ", "
                + SkillsHistory.COLUMN_UNIT_ID + ", " + SkillsHistory.COLUMN_NAME + ", "
                + SkillsHistory.COLUMN_DESCRIPTION + ", " + SkillsHistory.COLUMN_EFFECTS + ", " + SkillsHistory.COLUMN_IMAGE_ID + ", "
                + " ( $subSql ) IM " + ", " + SkillsHistory.COLUMN_LEVEL)
        val where: Where<SkillsHistory, Int> = qb.where()
        where.eq(SkillsHistory.COLUMN_UNIT_ID, skillsId)
        qb.orderBy(SkillsHistory.ID, false)

        val result:MutableList<SkillsHistory> = mutableListOf()
        val rawResults = this.queryRaw(
            qb.prepareStatementString(),
            arrayOf(DataType.INTEGER, DataType.STRING, DataType.INTEGER, DataType.STRING, DataType.STRING, DataType.STRING, DataType.INTEGER, DataType.BYTE_ARRAY, DataType.INTEGER))
        for (resultArray in rawResults) {
            val profile = SkillsHistory()
            profile.id = resultArray[0] as Int
            profile.version = resultArray[1].toString()
            profile.unitId = resultArray[2] as Int
            profile.name = resultArray[3].toString()
            profile.description = resultArray[4].toString()
            profile.effects = resultArray[5].toString()
            profile.imageId = resultArray[6] as Int
            if(resultArray[7] != null)
                profile.image = resultArray[7] as ByteArray
            profile.level = resultArray[8] as Int
            result.add(profile)
        }
        return result
    }

}