package com.mx.cosmo.orm

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.R
import com.mx.cosmo.orm.imp.SaintInfoDaoImp
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
        if(oldVersion <= 1){ // new : 5
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_VITALITY + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_AURA + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_TECH + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_HP + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_PHYS_ATTACK + " INTEGER ; ")
            getSaintInfoDao().executeRaw("ALTER TABLE " + SaintInfo.TABLE_NAME + " ADD COLUMN  " + SaintInfo.COLUMN_FURY_ATTACK + " INTEGER ; ")
        }
    }

    fun getSaintInfoDao(): SaintInfoDaoImp {
        return SaintInfoDaoImp(this)
    }

    companion object {
        // name of the database file for your application
        private const val DATABASE_NAME = "cosmo.db"
        // any time you make changes to your database objects, you may have to increase the database version
        private const val DATABASE_VERSION = 2
        private val CONFIG_CLASSES = arrayOf<Class<*>>(SaintInfo::class.java)
    }


}
