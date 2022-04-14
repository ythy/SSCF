package com.mx.cosmo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by maoxin on 2022/04/12.
 */
class SkillsPagerAdapter(fm: FragmentManager, private val datas:ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return datas[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

}