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
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class DetailActivity: BaseActivity() {

    companion object{
        const val TAG = "DetailActivity"
    }

    private val mHandler = MyHandler(this)
    private lateinit var mRootView:RootView
    private lateinit var mSaintInfo:SaintInfo

    @BindView(R.id.ll_skills)
    lateinit var mSkillsBox:LinearLayout

    @OnClick(R.id.btn_update)
    fun onBtnUpdateClickHandler(){
        mProgressDialog.show()
        Thread(Runnable {
            if( mSaintInfo.imageFullId <= 0) {
                val urlFull = URL(Setting.RESOURCES_REMOTE_URL + "${mSaintInfo.unitId}.png")
                try {
                    val bmp = FileUtils.loadRemoteImage(urlFull)
                    mSaintInfo.imageFull = FileUtils.getBitmapAsByteArray(bmp)
                    val imageInfo = mDbHelper.getImageInfoDao().createIfNotExists(ImageInfo(mSaintInfo.imageFull!!))
                    mSaintInfo.imageFullId = imageInfo.id

                } catch (e: IOException) {
                    Log.e(TAG, e.message)
                }
            }
            if( mSaintInfo.imageSmallId <= 0) {
                val urlSmall = URL(Setting.RESOURCES_REMOTE_URL + "${mSaintInfo.unitId}_0.png")
                try {
                    val bmp = FileUtils.loadRemoteImage(urlSmall)
                    val imageInfo =
                        mDbHelper.getImageInfoDao().createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                    mSaintInfo.imageSmallId = imageInfo.id
                } catch (e: IOException) {
                    Log.e(TAG, e.message)
                }
            }
             mDbHelper.getSaintInfoDao().update(mSaintInfo)

            val skillsList = mDbHelper.getSkillsInfoDao().querySkills(Utils.getIdFromUnitId(mSaintInfo.unitId))
            skillsList.forEach { skill ->
                val url = URL(Setting.RESOURCES_REMOTE_URL + "${skill.unitId}_0.png")
                try {
                    if(skill.imageId <= 0) {
                        val bmp = FileUtils.loadRemoteImage(url)
                        val imageInfo = mDbHelper.getImageInfoDao()
                            .createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                        skill.imageId = imageInfo.id
                        mDbHelper.getSkillsInfoDao().update(skill)
                    }
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
        this.mRootView = RootView(window.decorView.rootView)
        this.setBaseView()
    }

    private fun setBaseView(){
        mRootView.name.text = mSaintInfo.name
        mRootView.cloth.text = mSaintInfo.cloth
        mRootView.time.text = mSaintInfo.activeTime
        mRootView.description.text = mSaintInfo.description
        mRootView.type.text = mSaintInfo.type
        mRootView.lane.text = mSaintInfo.lane
        mRootView.power.text = mSaintInfo.detailInfo.power.toString()
        mRootView.tiers.text = String.format(resources.getString(R.string.saint_tiers), mSaintInfo.detailTier.tiersPVP, mSaintInfo.detailTier.tiersCrusade, mSaintInfo.detailTier.tiersPVE )
        mRootView.id.text = mSaintInfo.unitId.toString()
        mRootView.rateVit.text = mSaintInfo.detailInfo.vitalityRate.toString()
        mRootView.rateAura.text = mSaintInfo.detailInfo.auraRate .toString()
        mRootView.rateTech.text = mSaintInfo.detailInfo.techRate.toString()

        mRootView.vitality.text = mSaintInfo.detailInfo.vitality.toString()
        mRootView.aura.text = mSaintInfo.detailInfo.aura.toString()
        mRootView.tech.text = mSaintInfo.detailInfo.technique.toString()
        mRootView.hp.text = mSaintInfo.detailInfo.hp.toString()
        mRootView.physAttack.text = mSaintInfo.detailInfo.physAttack.toString()
        mRootView.furyAttack.text = mSaintInfo.detailInfo.furyAttack.toString()
        mRootView.physDefense.text = mSaintInfo.detailInfo.physDefense.toString()
        mRootView.furyResistance.text = mSaintInfo.detailInfo.furyResistance.toString()
        mRootView.accuracy.text = mSaintInfo.detailInfo.accuracy.toString()
        mRootView.evasion.text = mSaintInfo.detailInfo.evasion.toString()
        mRootView.hpRecovery.text = mSaintInfo.detailInfo.recoveryHP.toString()
        mRootView.cosmoRecovery.text = mSaintInfo.detailInfo.recoveryCosmo.toString()

        setSkill()
        setImages()
    }

    private fun setImages(){
        if(mSaintInfo.imageFull != null)
            mRootView.image.setImageBitmap(BitmapFactory.decodeByteArray(mSaintInfo.imageFull, 0, mSaintInfo.imageFull!!.size))
        else
            mRootView.image.setImageBitmap(null)
    }

    private fun setSkill(){
        mSkillsBox.removeAllViews()
        val skillsList = mDbHelper.getSkillsInfoDao().querySkills(Utils.getIdFromUnitId(mSaintInfo.unitId))
        skillsList.forEach {
            val child = LayoutInflater.from(this@DetailActivity).inflate(
                R.layout.layout_skills, mSkillsBox, false)
            mSkillsBox.addView(child)
            val skills = Skills(child)
            skills.name.text = it.name
            skills.description.text = it.description
            if(it.image != null)
                skills.images.setImageBitmap(BitmapFactory.decodeByteArray(it.image, 0, it.image!!.size))
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
                    activity.setImages()
                    activity.setSkill()
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

    class RootView constructor(view: View){

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