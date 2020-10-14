package com.mx.cosmo.di.module

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

/**
 * Created by maoxin on 2017/2/20.
 */

@Module(includes = arrayOf(DBModule::class))
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("SSCF", Context.MODE_PRIVATE)
    }


}
