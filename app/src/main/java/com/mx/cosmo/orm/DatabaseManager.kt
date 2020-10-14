package com.mx.cosmo.orm

/**
 * Created by maoxin on 2018/7/20.
 */

class DatabaseManager {
    companion object {

         fun <T : com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper> getHelper(context: android.content.Context, openHelperClass: Class<T>): T {
            if (context is android.app.Application) {
                return com.j256.ormlite.android.apptools.OpenHelperManager.getHelper(context, openHelperClass)
            } else {
                return com.j256.ormlite.android.apptools.OpenHelperManager.getHelper(context.applicationContext, openHelperClass)
            }
        }
    }

}
