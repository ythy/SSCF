package com.mx.cosmo.orm.imp

import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.mx.cosmo.orm.vo.SaintInfo

class SaintInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SaintInfo, Int>(SaintInfoDaoImp.getDao(orm)) {

    companion object{
        var dao: Dao<SaintInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SaintInfo, Int> {
            return if(dao == null)
                orm.getDao(SaintInfo::class.java)
            else
                dao!!
        }
    }

    fun createOrUpdateSaint(saintInfo: SaintInfo){
        val current = this.queryForEq(SaintInfo.COLUMN_UNIT_ID, saintInfo.unitId)
        if(current != null && current.isNotEmpty()){
            saintInfo.id = current[0].id
            this.update(saintInfo)
        }else{
            this.create(saintInfo)
        }
    }

    fun querySaintList(orderBy:String, isAsc:Boolean = false):List<SaintInfo>{
        val query = this.queryBuilder()
        query.orderBy(orderBy, isAsc)
        return this.query(query.prepare())
    }


}

