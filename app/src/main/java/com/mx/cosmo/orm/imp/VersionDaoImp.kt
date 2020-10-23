package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.mx.cosmo.orm.vo.Version

class VersionDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<Version, Int>(getDao(orm)) {

    companion object {
        var dao: Dao<Version, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<Version, Int> {
            return if (dao == null)
                orm.getDao(Version::class.java)
            else
                dao!!
        }
    }

    fun getLastVersion():Version?{
        val qb = this.queryBuilder()
        qb.orderBy(Version.ID, false)
        return qb.queryForFirst()
    }

}