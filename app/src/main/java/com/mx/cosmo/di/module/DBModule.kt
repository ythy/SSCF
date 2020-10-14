package com.mx.cosmo.di.module

import android.content.Context
import com.mx.cosmo.orm.DataBaseHelper
import com.mx.cosmo.orm.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by maoxin on 2018/9/27.
 */
@Module
class DBModule {

    @Provides
    @Singleton
    internal fun  provideDataBaseHelper(context: Context): DataBaseHelper {
        return  DatabaseManager.getHelper(context, DataBaseHelper::class.java)
    }

}