package com.mx.cosmo.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.orm.vo.SaintInfo

class MainAdapter(val context: Context, private var datalist:List<SaintInfo>): BaseAdapter() {


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
                R.layout.adapter_main_list, parent, false)
            component = Component(convertView)
            convertView?.tag = component
        } else
            component = convertView.tag as Component

        try {
            if(datalist[position].imageSmall != null)
                component.header.setImageBitmap(BitmapFactory.decodeByteArray(datalist[position].imageSmall, 0, datalist[position].imageSmall!!.size))
            else
                component.header.setImageBitmap(null)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        component.name.text = datalist[position].name
        component.vit.text =  datalist[position].vitalityRate.toString()
        component.aura.text =  datalist[position].auraRate.toString()
        component.tech.text =  datalist[position].techRate.toString()
        component.pvp.text =  datalist[position].tiersPVP
        component.pve.text =  datalist[position].tiersPVE
        return convertView!!
    }

    class Component(view: View) {

        @BindView(R.id.ivHeader)
        lateinit var header: ImageView

        @BindView(R.id.tv_name)
        lateinit var name: TextView

        @BindView(R.id.tv_vit)
        lateinit var vit: TextView

        @BindView(R.id.tv_aura)
        lateinit var aura: TextView

        @BindView(R.id.tv_tech)
        lateinit var tech: TextView

        @BindView(R.id.tv_pvp)
        lateinit var pvp: TextView

        @BindView(R.id.tv_pve)
        lateinit var pve: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }



}