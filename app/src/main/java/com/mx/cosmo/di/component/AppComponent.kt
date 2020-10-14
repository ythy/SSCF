package com.mx.cosmo.di.component

import android.content.SharedPreferences
import com.mx.cosmo.di.module.AppModule
import com.mx.cosmo.orm.DataBaseHelper
import javax.inject.Singleton
import dagger.Component

/**
 * Created by maoxin on 2017/2/20.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun dataBaseHelper(): DataBaseHelper
    fun getSharedPreferences(): SharedPreferences
}
