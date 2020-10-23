package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.mx.cosmo.orm.vo.TierInfo


class TierInfoDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<TierInfo, Int>(getDao(orm)) {

    companion object {
        var dao: Dao<TierInfo, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<TierInfo, Int> {
            return if (dao == null)
                orm.getDao(TierInfo::class.java)
            else
                dao!!
        }
    }

    fun createTier(tier:TierInfo){
        val qb = this.queryBuilder()
        qb.orderBy(TierInfo.ID, false)
        qb.where().eq(TierInfo.COLUMN_SAINT_ID, tier.saintId)
        val oldTier = qb.queryForFirst()
        if(oldTier == null){
            this.create(tier)
        }else if(!( oldTier.tiersPVP == tier.tiersPVP
                    && oldTier.tiersCrusade == tier.tiersCrusade
                    && oldTier.tiersPVE == tier.tiersPVE )){
            this.create(tier)
        }
    }

}