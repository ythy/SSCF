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

    @BindView(R.id.iv_detail)
    lateinit var mImageViewDetail:ImageView

    @BindView(R.id.ll_skills)
    lateinit var mSkillsBox: LinearLayout

    @BindView(R.id.tv_name)
    lateinit var mTextViewName:TextView

    @BindView(R.id.tv_id)
    lateinit var mTextViewID:TextView

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
        ButterKnife.bind(this)
        this.mSaintInfo = intent.getParcelableExtra("saint")
        this.setBaseView()
        this.setSkill()
    }

    private fun setBaseView(){
        mTextViewName.text = mSaintInfo.name
        mTextViewID.text = mSaintInfo.unitId.toString()
        val imageDir = File(Environment.getExternalStorageDirectory(), Setting.RESOURCES_FILE_PATH_FULL)
        val file = File(imageDir.path,  "${mSaintInfo.unitId}.png")
        if (file.exists()) {
            val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
            mImageViewDetail.setImageBitmap(bmp)
        } else
            mImageViewDetail.setImageBitmap(null)
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

}