package com.mx.cosmo.common

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import com.mx.cosmo.orm.vo.SaintHistory
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.SkillsInfo
import com.mx.cosmo.orm.vo.TierInfo
import org.json.JSONTokener
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by maoxin on 2018/10/9.
 */

class Utils {

    companion object{

        fun loadJSONFromAsset(context: Context, filename:String): Pair<ArrayList<SaintInfo>, ArrayList<SkillsInfo>>{
            var json: String? = null
            try {
                val file:InputStream? = context.assets.open(filename)
                val size = file!!.available()
                val buffer = ByteArray(size)
                file.read(buffer)
                file.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return parseJsonData(JSONObject(json))
        }

        fun parseJsonData(json:JSONObject, version:String = Setting.DEFAULT_DATA_DATE):Pair<ArrayList<SaintInfo>, ArrayList<SkillsInfo>>{
            val saintList = ArrayList<SaintInfo>()
            val skillsList = ArrayList<SkillsInfo>()
            val data = json.get("saints") as JSONArray
            for( i in 0 until data.length()){
                val obj = data.get(i) as JSONObject
                val saint = SaintInfo()
                val tierInfo = TierInfo()
                val saintDetail = SaintHistory()
                saintDetail.version = version
                tierInfo.version =  version
                saint.name = if (obj.has("name")) obj.getString("name") else ""
                saint.description = if (obj.has("description")) obj.getString("description") else ""
                saint.unitId  = if (obj.has("id")) obj.getInt("id") else 0
                saintDetail.saintId = saint.unitId
                tierInfo.saintId =  saint.unitId
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
                                saintDetail.power  = detail.getInt("uber")
                            }
                            "Active Time" -> {
                                val time = detail.getString("uber").trim()
                                saint.activeTime = convertActiveTime(time)
                            }
                            "Vitality Growth Rate" -> {
                                saintDetail.vitalityRate = detail.getDouble("uber")
                            }
                            "Aura Growth Rate" -> {
                                saintDetail.auraRate = detail.getDouble("uber")
                            }
                            "Tech. Growth Rate" -> {
                                saintDetail.techRate = detail.getDouble("uber")
                            }

                            "Vitality" -> {
                                saintDetail.vitality = detail.getInt("uber")
                            }
                            "Aura" -> {
                                saintDetail.aura = detail.getInt("uber")
                            }
                            "Technique" -> {
                                saintDetail.technique = detail.getInt("uber")
                            }
                            "Max HP" -> {
                                saintDetail.hp = detail.getInt("uber")
                            }
                            "Phys. Attack" -> {
                                saintDetail.physAttack = detail.getInt("uber")
                            }
                            "Fury Attack" -> {
                                saintDetail.furyAttack = detail.getInt("uber")
                            }

                            "Phys. Defense" -> {
                                saintDetail.physDefense = detail.getInt("uber")
                            }
                            "Fury Resistance" -> {
                                saintDetail.furyResistance = detail.getInt("uber")
                            }
                            "Accuracy" -> {
                                saintDetail.accuracy = detail.getInt("uber")
                            }
                            "Evasion" -> {
                                saintDetail.evasion = detail.getInt("uber")
                            }
                            "HP Recovery" -> {
                                saintDetail.recoveryHP = detail.getInt("uber")
                            }
                            "Cosmo Recovery" -> {
                                saintDetail.recoveryCosmo = detail.getInt("uber")
                            }
                            "Cloth kind" -> {
                                saint.cloth = detail.getString("uber")
                            }
                        }
                    }
                }

                val tiers = if (obj.has("tiers"))  obj.get("tiers").toString() else ""
                if(tiers.length < 4){
                    tierInfo.tiersPVP = tiers
                }else{
                    val tiersObj = JSONObject(tiers)
                    tierInfo.tiersPVP = tiersObj.getString("PVP")
                    tierInfo.tiersCrusade =  tiersObj.getString("Crusade")
                    tierInfo.tiersPVE =  tiersObj.getString("PVE")
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
                saint.detailInfo = saintDetail
                saint.detailTier = tierInfo
                saintList.add(saint)
            }
            return Pair(saintList, skillsList)
        }

        @Suppress("unused")
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

        fun loadRemoteJsonData(webUrl:String):JSONObject{
            val string = readUrl(webUrl)
            val tokenizer = JSONTokener(string)
            return JSONObject(tokenizer)
        }

        @Throws(Exception::class)
        private fun readUrl(urlString: String): String {
            var reader: BufferedReader? = null
            try {
                val url = URL(urlString)
                reader = BufferedReader(InputStreamReader(url.openStream()))
                val buffer = StringBuffer()
                var read: Int
                val chars = CharArray(1024)

                read = reader.read(chars)
                while (read != -1){
                    buffer.append(chars, 0, read)
                    read = reader.read(chars)
                }
                return buffer.toString()

            } finally {
                reader?.close()
            }
        }

        fun convertActiveTime(input:String):String{
            var result = ""
            try {
                if(input != "" && input != "A long time ago..."){
                    val date = SimpleDateFormat("MM/dd/yyyy", Locale.CHINESE).parse(input)
                    result = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).format(date)
                }
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }finally {
                return result
            }
        }
    }


}