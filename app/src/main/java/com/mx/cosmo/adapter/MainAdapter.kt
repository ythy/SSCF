package com.mx.cosmo.adapter

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.common.Setting
import com.mx.cosmo.orm.vo.SaintInfo
import java.io.File

class MainAdapter(val context: Context, var datalist:List<SaintInfo>): BaseAdapter() {


    override fun getItem(position: Int): Any {
        return datalist.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return datalist.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var component: Component
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.adapter_main_list, null)
            component = Component(convertView)
            convertView?.tag = component
        } else
            component = convertView.tag as Component

        try {
            val imageDir = File(Environment.getExternalStorageDirectory(), Setting.RESOURCES_FILE_PATH_SMALL)
            val file = File(imageDir.path,  "${datalist[position].unitId}_0.png")
            if (file.exists()) {
                val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.fromFile(file))
                component.header.setImageBitmap(bmp)
            } else
                component.header.setImageBitmap(null)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        component.name.text = datalist[position].name
//        component.type.text = when {
//            datalist[position].type == "Vitality" -> "Vit."
//            datalist[position].type == "Technique" -> "Tec."
//            else -> datalist[position].type
//        }
//        component.lane.text = when {
//            datalist[position].lane == "Front" -> "Front"
//            datalist[position].lane == "Middle" -> "Mid."
//            else -> datalist[position].lane
//        }
        component.vit.text =  datalist[position].vitalityRate.toString()
        component.aura.text =  datalist[position].auraRate.toString()
        component.tech.text =  datalist[position].techRate.toString()
        component.pvp.text =  datalist[position].tiersPVP
        component.pve.text =  datalist[position].tiersPVE
        return convertView!!
    }

    class Component{

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

        constructor(view: View){
            ButterKnife.bind(this, view)
        }
    }



}