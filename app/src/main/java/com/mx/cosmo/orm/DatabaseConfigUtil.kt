package com.mx.cosmo.orm


import com.mx.cosmo.orm.vo.*

/**
 * Created by maoxin on 2018/7/20.
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */

class DatabaseConfigUtil{

    companion object {
        private val classes = arrayOf<Class<*>>(SaintInfo::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            com.j256.ormlite.android.apptools.OrmLiteConfigUtil.writeConfigFile("ormlite_config.txt", com.mx.cosmo.orm.DatabaseConfigUtil.Companion.classes)
        }
    }

}
