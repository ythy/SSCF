package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.field.DataType
import com.j256.ormlite.stmt.Where
import com.mx.cosmo.orm.vo.ImageInfo

class ImageInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<ImageInfo, Int>(getDao(orm)) {

    companion object {
        var dao: Dao<ImageInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<ImageInfo, Int> {
            return if (dao == null)
                orm.getDao(ImageInfo::class.java)
            else
                dao!!
        }
    }

}