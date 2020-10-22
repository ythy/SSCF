package com.mx.cosmo.di.module


import android.app.ProgressDialog
import com.mx.cosmo.activity.BaseActivity
import com.mx.cosmo.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by maoxin on 2018/9/28.
 */
@Suppress("unused")
@Module
class ActivityModule(private val activity:BaseActivity ) {

    @Provides
    @ActivityScope
    internal fun provideProgressDialog(): ProgressDialog {
        val pd = ProgressDialog(activity)
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.setMessage("请稍等。。。")
        pd.isIndeterminate = false
        pd.setCancelable(false)
        return pd
    }

}