package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.j256.ormlite.stmt.Where
import com.mx.cosmo.orm.vo.SkillsInfo

class SkillsInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SkillsInfo, Int>(getDao(orm)) {

    companion object{
        var dao: Dao<SkillsInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SkillsInfo, Int> {
            return if(dao == null)
                orm.getDao(SkillsInfo::class.java)
            else
                dao!!
        }
    }

    fun createOrUpdateSkills(skills: SkillsInfo){
        val current = this.queryForEq(SkillsInfo.COLUMN_UNIT_ID, skills.unitId)
        if(current != null && current.isNotEmpty()){
            skills.id = current[0].id
            this.update(skills)
        }else{
            this.create(skills)
        }
    }

    fun querySkills(info: SkillsInfo, orderBy:String = SkillsInfo.ID, isAsc:Boolean = false):List<SkillsInfo>{
        val qb = this.queryBuilder()
        qb.orderBy(orderBy, isAsc)
        qb.selectRaw(SkillsInfo.ID + ", " + SkillsInfo.COLUMN_SAINT_ID + ", "+ SkillsInfo.COLUMN_NAME + ", "
                + SkillsInfo.COLUMN_DESCRIPTION + ", " + SkillsInfo.COLUMN_EFFECTS + ", " + SkillsInfo.COLUMN_IMAGE)
        val where: Where<SkillsInfo, Int> = qb.where()
        where.eq(SkillsInfo.COLUMN_SAINT_ID, info.saintId)

        val result:MutableList<SkillsInfo> = mutableListOf()
        val rawResults = this.queryRaw(
            qb.prepareStatementString(),
            arrayOf(DataType.INTEGER, DataType.INTEGER, DataType.STRING, DataType.STRING, DataType.STRING, DataType.BYTE_ARRAY))
        for (resultArray in rawResults) {
            val profile = SkillsInfo()
            profile.id = resultArray[0] as Int
            profile.saintId = resultArray[1] as Int
            profile.name = resultArray[2].toString()
            profile.description = resultArray[3].toString()
            profile.effects = resultArray[3].toString()
            profile.image = resultArray[4] as ByteArray
            result.add(profile)
        }
        return result
    }

}