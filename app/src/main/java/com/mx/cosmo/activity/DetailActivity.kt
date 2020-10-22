package com.mx.cosmo.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mx.cosmo.R
import com.mx.cosmo.common.FileUtils
import com.mx.cosmo.common.Setting
import com.mx.cosmo.common.Utils
import com.mx.cosmo.orm.vo.ImageInfo
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.SkillsInfo
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class DetailActivity: BaseActivity() {

    companion object{
        const val TAG = "DetailActivity"
    }

    private val mHandler = MyHandler(this)
    private lateinit var mSaintInfo:SaintInfo

    @BindView(R.id.ll_skills)
    lateinit var mSkillsBox:LinearLayout

    @OnClick(R.id.btn_update)
    fun onBtnUpdateClickHandler(){
        mProgressDialog.show()
        Thread(Runnable {
            val urlFull = URL(Setting.RESOURCES_REMOTE_URL + "${mSaintInfo.unitId}.png")
            try {
                val bmp = FileUtils.loadRemoteImage(urlFull)
                val imageInfo = mDbHelper.getImageInfoDao().createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                mSaintInfo.imageFullId = imageInfo.id
            }catch (e:IOException){
                Log.e(TAG, e.message)
            }

            val urlSmall = URL(Setting.RESOURCES_REMOTE_URL + "${mSaintInfo.unitId}_0.png")
            try {
                val bmp = FileUtils.loadRemoteImage(urlSmall)
                val imageInfo = mDbHelper.getImageInfoDao().createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                mSaintInfo.imageSmallId = imageInfo.id
            }catch (e:IOException){
                Log.e(TAG, e.message)
            }

             mDbHelper.getSaintInfoDao().update(mSaintInfo)

            val skillsList = mDbHelper.getSkillsInfoDao().queryForEq(
                SkillsInfo.COLUMN_SAINT_ID, Utils.getIdFromUnitId(mSaintInfo.unitId))
            skillsList.forEach { skill ->
                val url = URL(Setting.RESOURCES_REMOTE_URL + "${skill.unitId}_0.png")
                try {
                    val bmp = FileUtils.loadRemoteImage(url)
                    skill.image = FileUtils.getBitmapAsByteArray(bmp)
                    mDbHelper.getSkillsInfoDao().createOrUpdateSkills(skill)
                }catch (e:IOException){
                    Log.e(MainActivity.TAG, e.message)
                }
            }
            mHandler.sendEmptyMessage(1)
        }).start()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val id = intent.getIntExtra("saint_id", 0)
        this.mSaintInfo = mDbHelper.getSaintInfoById(id)
        ButterKnife.bind(this)
        this.setBaseView()
        this.setSkill()
    }

    private fun setBaseView(){
        val component = MainUI(window.decorView.rootView)
        component.name.text = mSaintInfo.name
        component.cloth.text = mSaintInfo.cloth
        component.time.text = mSaintInfo.activeTime
        component.description.text = mSaintInfo.description
        component.type.text = mSaintInfo.type
        component.lane.text = mSaintInfo.lane
        component.power.text = mSaintInfo.power.toString()
        component.tiers.text = String.format(resources.getString(R.string.saint_tiers), mSaintInfo.tiersPVP, mSaintInfo.tiersCrusade, mSaintInfo.tiersPVE )
        component.id.text = mSaintInfo.unitId.toString()
        component.rateVit.text = mSaintInfo.vitalityRate.toString()
        component.rateAura.text = mSaintInfo.auraRate .toString()
        component.rateTech.text = mSaintInfo.techRate.toString()

        component.vitality.text = mSaintInfo.vitality.toString()
        component.aura.text = mSaintInfo.aura.toString()
        component.tech.text = mSaintInfo.technique.toString()
        component.hp.text = mSaintInfo.hp.toString()
        component.physAttack.text = mSaintInfo.physAttack.toString()
        component.furyAttack.text = mSaintInfo.furyAttack.toString()
        component.physDefense.text = mSaintInfo.physDefense.toString()
        component.furyResistance.text = mSaintInfo.furyResistance.toString()
        component.accuracy.text = mSaintInfo.accuracy.toString()
        component.evasion.text = mSaintInfo.evasion.toString()
        component.hpRecovery.text = mSaintInfo.recoveryHP.toString()
        component.cosmoRecovery.text = mSaintInfo.recoveryCosmo.toString()
        if(mSaintInfo.imageFull != null)
            component.image.setImageBitmap(BitmapFactory.decodeByteArray(mSaintInfo.imageFull, 0, mSaintInfo.imageFull!!.size))
        else
            component.image.setImageBitmap(null)
    }

    private fun setSkill(){
        val skillsList = mDbHelper.getSkillsInfoDao().queryForEq(SkillsInfo.COLUMN_SAINT_ID,
             Utils.getIdFromUnitId(mSaintInfo.unitId))
        skillsList.forEach {
            val child = LayoutInflater.from(this@DetailActivity).inflate(
                R.layout.layout_skills, mSkillsBox, false)
            mSkillsBox.addView(child)
            val skills = Skills(child)
            skills.name.text = it.name
            skills.description.text = it.description
            if(it.image.isNotEmpty())
                skills.images.setImageBitmap(BitmapFactory.decodeByteArray(it.image, 0, it.image.size))
        }
    }

    class MyHandler constructor(activity:DetailActivity):Handler(){
        private val weakReference:WeakReference<DetailActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val activity = weakReference.get()!!
            when {
                msg.what == 1 -> {
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, "update done", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    class Skills constructor(view: View){

        @BindView(R.id.iv_skill_image)
        lateinit var images:ImageView

        @BindView(R.id.tv_skills_name)
        lateinit var name:TextView

        @BindView(R.id.tv_skill_description)
        lateinit var description:TextView

        init {
            ButterKnife.bind(this, view)
        }

    }

    class MainUI constructor(view: View){

        @BindView(R.id.iv_detail)
        lateinit var image:ImageView

        @BindView(R.id.tv_name)
        lateinit var name:TextView

        @BindView(R.id.tv_cloth)
        lateinit var cloth:TextView

        @BindView(R.id.tv_time)
        lateinit var time:TextView

        @BindView(R.id.tv_description)
        lateinit var description:TextView

        @BindView(R.id.tv_id)
        lateinit var id:TextView

        @BindView(R.id.tv_tiers)
        lateinit var tiers:TextView

        @BindView(R.id.tv_type)
        lateinit var type:TextView

        @BindView(R.id.tv_lane)
        lateinit var lane:TextView

        @BindView(R.id.tv_power)
        lateinit var power:TextView

        @BindView(R.id.tv_rate_vit)
        lateinit var rateVit:TextView

        @BindView(R.id.tv_rate_aura)
        lateinit var rateAura:TextView

        @BindView(R.id.tv_rate_tech)
        lateinit var rateTech:TextView

        @BindView(R.id.tv_vitality)
        lateinit var vitality:TextView

        @BindView(R.id.tv_aura)
        lateinit var aura:TextView

        @BindView(R.id.tv_tech)
        lateinit var tech:TextView

        @BindView(R.id.tv_hp)
        lateinit var hp:TextView

        @BindView(R.id.tv_phys_attack)
        lateinit var physAttack:TextView

        @BindView(R.id.tv_fury_attack)
        lateinit var furyAttack:TextView

        @BindView(R.id.tv_phys_defense)
        lateinit var physDefense:TextView

        @BindView(R.id.tv_fury_resistance)
        lateinit var furyResistance:TextView

        @BindView(R.id.tv_accuracy)
        lateinit var accuracy:TextView

        @BindView(R.id.tv_evasion)
        lateinit var evasion:TextView

        @BindView(R.id.tv_hp_recovery)
        lateinit var hpRecovery:TextView

        @BindView(R.id.tv_cosmo_recovery)
        lateinit var cosmoRecovery:TextView

        init {
            ButterKnife.bind(this, view)
        }

    }

}