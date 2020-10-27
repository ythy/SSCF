package com.mx.cosmo.di.module

import android.support.v7.app.AlertDialog
import com.mx.cosmo.activity.BaseActivity
import com.mx.cosmo.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * Created by maoxin on 2018/9/28.
 */
@Suppress("unused")
@Module
class ActivityModule(private val activity:BaseActivity ) {

    @Provides
    @ActivityScope
    internal fun provideProgressDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val dialogView = inflater!!.inflate(com.mx.cosmo.R.layout.dialog_progress, activity.window.decorView.rootView as ViewGroup, false)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        return dialogBuilder.create()
    }

}