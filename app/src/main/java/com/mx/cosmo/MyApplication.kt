package com.mx.cosmo

import android.app.Application
import com.mx.cosmo.di.component.AppComponent
import com.mx.cosmo.di.component.DaggerAppComponent
import com.mx.cosmo.di.module.AppModule

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}