package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.j256.ormlite.stmt.Where
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

    fun createNewSkills(skillsInfo: SkillsInfo){
        val current = this.queryForEq(SkillsInfo.COLUMN_UNIT_ID, skillsInfo.unitId)
        if(current == null || current.isEmpty()){
            this.create(skillsInfo)
        }
    }


    fun querySkills(saintId:Int):List<SkillsInfo>{
        val qb = this.queryBuilder()
        qb.selectRaw(SkillsInfo.ID + ", " + SkillsInfo.COLUMN_SAINT_ID + ", "
                + SkillsInfo.COLUMN_UNIT_ID )
        val where: Where<SkillsInfo, Int> = qb.where()
        where.eq(SkillsInfo.COLUMN_SAINT_ID, saintId)

        val result:MutableList<SkillsInfo> = mutableListOf()
        val rawResults = this.queryRaw(
            qb.prepareStatementString(),
            arrayOf(DataType.INTEGER, DataType.INTEGER, DataType.INTEGER))
        for (resultArray in rawResults) {
            val profile = SkillsInfo()
            profile.id = resultArray[0] as Int
            profile.saintId = resultArray[1] as Int
            profile.unitId = resultArray[2] as Int
            result.add(profile)
        }
        return result
    }

}