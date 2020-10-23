package com.mx.cosmo.orm

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.field.DataType
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.mx.cosmo.R
import com.mx.cosmo.orm.imp.*
import com.mx.cosmo.orm.vo.*
import java.sql.SQLException


/**
 * Created by maoxin on 2020/10/10.
 */

class DataBaseHelper (context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config) {

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    override fun onCreate(database: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            for (configClass in CONFIG_CLASSES) {
                TableUtils.createTableIfNotExists(connectionSource, configClass)
            }
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }

    }

    override fun onUpgrade(database: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        if(oldVersion <= 1){
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_VITALITY + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_AURA + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_TECH + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_HP + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_PHYS_ATTACK + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_FURY_ATTACK + " INTEGER ; ")
        }
        if(oldVersion <= 2){
            getSkillsInfoDao().executeRaw(" CREATE TABLE " + SkillsInfo.TABLE_NAME + " ( "
                    + SkillsInfo.ID + " INTEGER PRIMARY KEY , "
                    + SkillsInfo.COLUMN_SAINT_ID + " INTEGER , "
                    + SkillsInfo.COLUMN_NAME + " VARCHAR , "
                    + SkillsInfo.COLUMN_DESCRIPTION + " VARCHAR , "
                    + SkillsInfo.COLUMN_EFFECTS + " VARCHAR , "
                    + SkillsInfo.COLUMN_IMAGE + " BLOB )")
        }
        if(oldVersion <= 3){
            getSkillsInfoDao().executeRaw("ALTER TABLE " + SkillsInfo.TABLE_NAME + " ADD COLUMN  " + SkillsInfo.COLUMN_UNIT_ID + " INTEGER ; ")
        }
        if(oldVersion <= 4){
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_PHYS_DEFENSE + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_FURY_RESISTANCE + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_ACCURACY + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_EVASION + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_HP_RECOVERY + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_COSMO_RECOVERY + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_CLOTH + " VARCHAR ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_DESCRIPTION + " VARCHAR ; ")
        }
        if(oldVersion <= 5){
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_IMAGE_SMALL_ID + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_IMAGE_FULL_ID + " INTEGER ; ")
            getSkillsInfoDao().executeRaw("ALTER TABLE " + SkillsInfo.TABLE_NAME + " ADD COLUMN  " + SkillsInfo.COLUMN_IMAGE_ID + " INTEGER ; ")
            getImageInfoDao().executeRaw(" CREATE TABLE " + ImageInfo.TABLE_NAME + " ( "
                    + ImageInfo.ID + " INTEGER PRIMARY KEY , "
                    + ImageInfo.COLUMN_IMAGE + " BLOB )")
        }

    }

    fun getSaintInfoDao(): SaintInfoDaoImp {
        return SaintInfoDaoImp(this)
    }

    fun getSkillsInfoDao(): SkillsInfoDaoImp {
        return SkillsInfoDaoImp(this)
    }

    fun getImageInfoDao(): ImageInfoDaoImp {
        return ImageInfoDaoImp(this)
    }

    fun getVersionDao(): VersionDaoImp {
        return VersionDaoImp(this)
    }

    fun getTierInfoDao(): TierInfoDaoImp {
        return TierInfoDaoImp(this)
    }

    fun getSaintHistoryDao(): SaintHistoryDaoImp {
        return SaintHistoryDaoImp(this)
    }


    fun getSaintInfoById(id:Int):SaintInfo{
        val saintInfo = getSaintInfoDao().queryForId(id)
        saintInfo.imageSmall = getImageInfoDao().queryForId(saintInfo.imageSmallId)?.image
        saintInfo.imageFull = getImageInfoDao().queryForId(saintInfo.imageFullId)?.image

        val qbDetail = this.getSaintHistoryDao().queryBuilder()
        qbDetail.orderBy(SaintHistory.ID, false)
        qbDetail.where().eq(SaintHistory.COLUMN_SAINT_ID, saintInfo.unitId)
        saintInfo.detailInfo = getSaintHistoryDao().queryForFirst(qbDetail.prepare())

        val qbTier = this.getTierInfoDao().queryBuilder()
        qbTier.orderBy(TierInfo.ID, false)
        qbTier.where().eq(TierInfo.COLUMN_SAINT_ID, saintInfo.unitId)
        saintInfo.detailTier = getTierInfoDao().queryForFirst(qbTier.prepare())

        return saintInfo
    }


    companion object {
        // name of the database file for your application
        private const val DATABASE_NAME = "cosmo.db"
        // any time you make changes to your database objects, you may have to increase the database version
        private const val DATABASE_VERSION = 6
        private val CONFIG_CLASSES = arrayOf<Class<*>>(SaintInfo::class.java, SkillsInfo::class.java, ImageInfo::class.java,
            Version::class.java, SaintHistory::class.java, TierInfo::class.java)
    }


}
