package com.mx.cosmo.di.component

import com.mx.cosmo.activity.BaseActivity
import com.mx.cosmo.di.module.ActivityModule
import com.mx.cosmo.di.scope.ActivityScope
import dagger.Component

/**
 * Created by maoxin on 2018/9/28.
 */
@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun injectActivity(activity:BaseActivity)
}