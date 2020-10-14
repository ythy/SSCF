package com.mx.cosmo.activity

import android.Manifest
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mx.cosmo.MyApplication
import com.mx.cosmo.di.component.DaggerActivityComponent
import com.mx.cosmo.di.module.ActivityModule
import com.mx.cosmo.orm.DataBaseHelper
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var mDbHelper: DataBaseHelper

    @Inject
    lateinit var mSP: SharedPreferences

    val MY_PERMISSIONS_REQUEST:Int = 102
    val permissions:Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
    }

    fun initDagger(){
        DaggerActivityComponent.builder()
                .appComponent((this.application as MyApplication).appComponent)
                .activityModule(ActivityModule(this))
                .build()
                .injectActivity(this)
    }

}