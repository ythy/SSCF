package com.mx.cosmo.common

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import android.net.ConnectivityManager
import android.util.Log
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.SkillsInfo

/**
 * Created by maoxin on 2018/10/9.
 */
class Utils {

    companion object{

        fun loadJSONFromAsset(context: Context, filename:String): Pair<ArrayList<SaintInfo>, ArrayList<SkillsInfo>>{
            var json: String? = null
            try {
                var file:InputStream? = context.getAssets().open(filename)
                val size = file!!.available()
                val buffer = ByteArray(size)
                file.read(buffer)
                file.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            val saintList = ArrayList<SaintInfo>()
            val skillsList = ArrayList<SkillsInfo>()
            val datas = JSONObject(json).get("saints") as JSONArray
            var i = datas.length()
            while( i-- > 0){
                val obj = datas.get(i) as JSONObject
                val saint = SaintInfo()
                saint.name = if (obj.has("name")) obj.getString("name") else ""
                saint.description = if (obj.has("description")) obj.getString("description") else ""
                saint.unitId = if (obj.has("id")) obj.getInt("id") else 0
                val stats = if (obj.has("stats"))  obj.getJSONArray("stats") else null
                if(stats != null){
                    for(j in 0 until stats.length() ){
                        val detail = stats.get(j) as JSONObject
                        when(detail.getString("name")){
                            "Lane" -> {
                                saint.lane = detail.getString("uber")
                            }
                            "Type" -> {
                                saint.type = detail.getString("uber")
                            }
                            "Power" -> {
                                saint.power = detail.getInt("uber")
                            }
                            "Active Time" -> {
                                saint.activeTime = detail.getString("uber")
                            }
                            "Vitality Growth Rate" -> {
                                saint.vitalityRate = detail.getDouble("uber")
                            }
                            "Aura Growth Rate" -> {
                                saint.auraRate = detail.getDouble("uber")
                            }
                            "Tech. Growth Rate" -> {
                                saint.techRate = detail.getDouble("uber")
                            }

                            "Vitality" -> {
                                saint.vitality = detail.getInt("uber")
                            }
                            "Aura" -> {
                                saint.aura = detail.getInt("uber")
                            }
                            "Technique" -> {
                                saint.technique = detail.getInt("uber")
                            }
                            "Max HP" -> {
                                saint.hp = detail.getInt("uber")
                            }
                            "Phys. Attack" -> {
                                saint.physAttack = detail.getInt("uber")
                            }
                            "Fury Attack" -> {
                                saint.furyAttack = detail.getInt("uber")
                            }

                            "Phys. Defense" -> {
                                saint.physDefense = detail.getInt("uber")
                            }
                            "Fury Resistance" -> {
                                saint.furyResistance = detail.getInt("uber")
                            }
                            "Accuracy" -> {
                                saint.accuracy = detail.getInt("uber")
                            }
                            "Evasion" -> {
                                saint.evasion = detail.getInt("uber")
                            }
                            "HP Recovery" -> {
                                saint.recoveryHP = detail.getInt("uber")
                            }
                            "Cosmo Recovery" -> {
                                saint.recoveryCosmo = detail.getInt("uber")
                            }
                            "Cloth kind" -> {
                                saint.cloth = detail.getString("uber")
                            }
                        }
                    }
                }
                val tiers = if (obj.has("tiers"))  obj.getJSONObject("tiers") else null
                if(tiers != null){
                    saint.tiersPVP = tiers.getString("PVP")
                    saint.tiersCrusade =  tiers.getString("Crusade")
                    saint.tiersPVE =  tiers.getString("PVE")
                }
                val skills = if (obj.has("skills"))  obj.getJSONArray("skills") else null
                if(skills != null){
                    for(k in 0 until skills.length()){
                        val skillsInfo = SkillsInfo()
                        val skillObject =  skills.get(k) as JSONObject
                        skillsInfo.unitId = skillObject.getInt("id")
                        skillsInfo.saintId = getSkillIdFromUnitId(skillObject.getInt("id"))
                        skillsInfo.name = if (skillObject.has("name")) skillObject.getString("name") else ""
                        skillsInfo.description = if (skillObject.has("description")) skillObject.getString("description") else ""
                        skillsInfo.effects = ""
                        skillsList.add(skillsInfo)
                    }
                }
                saintList.add(saint)
            }
            return Pair(saintList, skillsList)
        }


        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun getIdFromUnitId(unitId:Int):Int{
            return unitId.toString().substring(3, 6).toInt()
        }

        private fun getSkillIdFromUnitId(unitId:Int):Int{
            return unitId.toString().substring(2, 5).toInt()
        }


    }


}