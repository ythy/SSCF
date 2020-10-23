package com.mx.cosmo.orm.imp

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.mx.cosmo.orm.vo.SaintHistory
import com.mx.cosmo.orm.vo.TierInfo

class SaintHistoryDaoImp constructor(orm: OrmLiteSqliteOpenHelper) : RuntimeExceptionDao<SaintHistory, Int>(getDao(orm)) {

    companion object {
        var dao: Dao<SaintHistory, Int>? = null
        fun getDao(orm: OrmLiteSqliteOpenHelper): Dao<SaintHistory, Int> {
            return if (dao == null)
                orm.getDao(SaintHistory::class.java)
            else
                dao!!
        }
    }

    fun createSaintDetail(detail: SaintHistory){
        val qb = this.queryBuilder()
        qb.orderBy(SaintHistory.ID, false)
        qb.where().eq(SaintHistory.COLUMN_SAINT_ID, detail.saintId)
        val oldDetail = qb.queryForFirst()
        if(oldDetail == null){
            this.create(detail)
        }else if(!( oldDetail.power == detail.power
                    && oldDetail.vitalityRate == detail.vitalityRate
                    && oldDetail.auraRate == detail.auraRate
                    && oldDetail.techRate == detail.techRate
                    && oldDetail.vitality == detail.vitality
                    && oldDetail.aura == detail.aura
                    && oldDetail.technique == detail.technique
                    && oldDetail.hp == detail.hp
                    && oldDetail.physAttack == detail.physAttack
                    && oldDetail.furyAttack == detail.furyAttack
                    && oldDetail.physDefense == detail.physDefense
                    && oldDetail.furyResistance == detail.furyResistance
                    && oldDetail.accuracy == detail.accuracy
                    && oldDetail.evasion == detail.evasion
                    && oldDetail.recoveryHP == detail.recoveryHP
                    && oldDetail.recoveryCosmo == detail.recoveryCosmo)){
            this.create(detail)
        }
    }
}