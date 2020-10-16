package com.mx.cosmo.activity

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mx.cosmo.R
import com.mx.cosmo.common.FileUtils
import com.mx.cosmo.common.Setting
import com.mx.cosmo.common.Utils
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.SkillsInfo
import java.io.File
import java.io.IOException
import java.net.URL

class DetailActivity: BaseActivity() {

    @BindView(R.id.ll_skills)
    lateinit var mSkillsBox:LinearLayout

    @OnClick(R.id.btn_update)
    fun onBtnUpdateClickHandler(){
        Thread(Runnable {
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
        }).start()
    }

    private lateinit var mSaintInfo:SaintInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        this.mSaintInfo = intent.getParcelableExtra("saint")
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

        val imageDir = File(Environment.getExternalStorageDirectory(), Setting.RESOURCES_FILE_PATH_FULL)
        val file = File(imageDir.path,  "${mSaintInfo.unitId}.png")
        if (file.exists()) {
            val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
            component.image.setImageBitmap(bmp)
        } else
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
            if(it.image?.size > 0)
                skills.images.setImageBitmap(BitmapFactory.decodeByteArray(it.image, 0, it.image.size))
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