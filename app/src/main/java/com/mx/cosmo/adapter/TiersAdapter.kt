package com.mx.cosmo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.orm.vo.TierInfo

class TiersAdapter(val context: Context, private var datalist:List<TierInfo>): BaseAdapter() {


    override fun getItem(position: Int): Any {
        return datalist[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return datalist.size
    }

    override fun getView(position: Int, viewParam: View?, parent: ViewGroup?): View {
        val component: Component
        var convertView = viewParam

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.adapter_tiers, parent, false)
            component = Component(convertView)
            convertView?.tag = component
        } else
            component = convertView.tag as Component

        component.pvp.text =  String.format(context.resources.getString(R.string.saint_tiers),  datalist[position].tiersPVP,  datalist[position].tiersCrusade,  datalist[position].tiersPVE )
        component.version.text =  datalist[position].version
        return convertView!!
    }

    class Component(view: View) {

        @BindView(R.id.tv_level)
        lateinit var pvp: TextView

        @BindView(R.id.tv_version)
        lateinit var version: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }



}