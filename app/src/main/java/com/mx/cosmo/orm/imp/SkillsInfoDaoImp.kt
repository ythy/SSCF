package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.j256.ormlite.stmt.Where
import com.mx.cosmo.orm.vo.ImageInfo
import com.mx.cosmo.orm.vo.SkillsInfo

class SkillsInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SkillsInfo, Int>(getDao(orm)) {

    companion object{
        private var dao: Dao<SkillsInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SkillsInfo, Int> {
            return if(dao == null)
                orm.getDao(SkillsInfo::class.java)
            else
                dao!!
        }
    }

    fun createNewSkills(skills: SkillsInfo){
        val current = this.queryForEq(SkillsInfo.COLUMN_UNIT_ID, skills.unitId)
        if(current == null || current.isEmpty()){
            this.create(skills)
        }
    }

    fun querySkills(saintId:Int):List<SkillsInfo>{
        val qb = this.queryBuilder()
        val subSql = " SELECT ${ImageInfo.COLUMN_IMAGE} FROM ${ImageInfo.TABLE_NAME} " +
                " WHERE ${ImageInfo.TABLE_NAME}.${ImageInfo.ID} = ${SkillsInfo.TABLE_NAME}.${SkillsInfo.COLUMN_IMAGE_ID} "
        qb.selectRaw(SkillsInfo.ID + ", " + SkillsInfo.COLUMN_SAINT_ID + ", "
                + SkillsInfo.COLUMN_UNIT_ID + ", " + SkillsInfo.COLUMN_NAME + ", "
                + SkillsInfo.COLUMN_DESCRIPTION + ", " + SkillsInfo.COLUMN_EFFECTS + ", " + SkillsInfo.COLUMN_IMAGE_ID + ", "
                + " ( $subSql ) ${SkillsInfo.COLUMN_IMAGE} " )
        val where: Where<SkillsInfo, Int> = qb.where()
        where.eq(SkillsInfo.COLUMN_SAINT_ID, saintId)

        val result:MutableList<SkillsInfo> = mutableListOf()
        val rawResults = this.queryRaw(
            qb.prepareStatementString(),
            arrayOf(DataType.INTEGER, DataType.INTEGER, DataType.INTEGER, DataType.STRING, DataType.STRING, DataType.STRING, DataType.INTEGER, DataType.BYTE_ARRAY))
        for (resultArray in rawResults) {
            val profile = SkillsInfo()
            profile.id = resultArray[0] as Int
            profile.saintId = resultArray[1] as Int
            profile.unitId = resultArray[2] as Int
            profile.name = resultArray[3].toString()
            profile.description = resultArray[4].toString()
            profile.effects = resultArray[5].toString()
            profile.imageId = resultArray[6] as Int
            if(resultArray[7] != null)
                profile.image = resultArray[7] as ByteArray
            result.add(profile)
        }
        return result
    }

}