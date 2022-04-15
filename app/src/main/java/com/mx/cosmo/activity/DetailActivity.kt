package com.mx.cosmo.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mx.cosmo.R
import com.mx.cosmo.adapter.BasicinfoPagerAdapter
import com.mx.cosmo.adapter.SkillsPagerAdapter
import com.mx.cosmo.adapter.TiersAdapter
import com.mx.cosmo.common.FileUtils
import com.mx.cosmo.common.Setting
import com.mx.cosmo.common.Utils
import com.mx.cosmo.fragment.BasicinfoFragment
import com.mx.cosmo.fragment.SkillsFragment
import com.mx.cosmo.fragment.SkillsFragment.Companion.REC_DATA_SKILLS_IMAGE
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

    @OnClick(R.id.btn_update)
    fun onBtnUpdateClickHandler(){
        mProgressDialog.show()
        Thread(Runnable {
            var updateCount = 0
            if( mSaintInfo.imageFullId <= 0) {
                val urlFull = URL(Setting.RESOURCES_REMOTE_URL + "${mSaintInfo.unitId}.png")
                try {
                    val bmp = FileUtils.loadRemoteImage(urlFull)
                    mSaintInfo.imageFull = FileUtils.getBitmapAsByteArray(bmp)
                    val imageInfo = mDbHelper.getImageInfoDao().createIfNotExists(ImageInfo(mSaintInfo.imageFull!!))
                    mSaintInfo.imageFullId = imageInfo.id
                    mDbHelper.getSaintInfoDao().update(mSaintInfo)
                    updateCount++
                } catch (e: IOException) {
                    Log.e(TAG, e.message)
                }
            }
            val skillsList = mDbHelper.getSkillsInfoDao().querySkills(mSaintInfo.unitId)
            skillsList.forEach { skill ->
                val url = URL(Setting.RESOURCES_REMOTE_URL + "${skill.unitId}_0.png")
                try {
                    if(skill.imageId <= 0) {
                        val bmp = FileUtils.loadRemoteImage(url)
                        val imageInfo = mDbHelper.getImageInfoDao()
                            .createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                        skill.imageId = imageInfo.id
                        mDbHelper.getSkillsInfoDao().update(skill)
                        updateCount++
                    }
                }catch (e:IOException){
                    Log.e(MainActivity.TAG, e.message)
                }
            }
            if(updateCount > 0)
                mHandler.sendEmptyMessage(1)
            else
                mHandler.sendEmptyMessage(2)
        }).start()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        ButterKnife.bind(this)
        val id = intent.getIntExtra("saint_id", 0)
        this.mSaintInfo = mDbHelper.getSaintInfoById(id)
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
        mRootView.id.text = mSaintInfo.unitId.toString()

        setTiers()
        setImages()
        setBasicPager()
        setSkillsPager()
    }

    private fun setTiers(){
        val tiers = mDbHelper.getTierInfoDao().getTierBySanit(mSaintInfo.unitId)
        val adapter = TiersAdapter(this, tiers)
        mRootView.tiers.adapter = adapter
    }

    private fun setImages(){
        if(mSaintInfo.imageFull != null){
            mRootView.image.setImageBitmap(BitmapFactory.decodeByteArray(mSaintInfo.imageFull, 0, mSaintInfo.imageFull!!.size))
            mRootView.image.visibility = View.VISIBLE
        }else{
            mRootView.image.setImageBitmap(null)
            mRootView.image.visibility = View.GONE
        }
    }

    private fun setBasicPager(){

        val basicHis = mDbHelper.getSaintHistoryDao().querySaintHistory(mSaintInfo.unitId)
        val fragments = arrayListOf<Fragment>()

        basicHis.forEachIndexed { index, it ->
            val bundle = Bundle()
            bundle.putInt("id", it.id)
            if( index + 1 < basicHis.size){
                bundle.putInt("lastId", basicHis[index + 1].id)
            }else{
                bundle.putInt("lastId", -1)
            }
            val basicFragment = BasicinfoFragment()
            basicFragment.arguments = bundle
            fragments.add(basicFragment)
        }

        mRootView.basicPager.adapter = BasicinfoPagerAdapter(supportFragmentManager, fragments)
        mRootView.basicPager.currentItem = 0

        val params = mRootView.basicPager.getLayoutParams()
        params.height = Utils.dip2px(this, 400f)
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        mRootView.basicPager.requestLayout()
    }

    private fun setSkillsPager(){
        val skillsList = mDbHelper.getSkillsInfoDao().querySkills(mSaintInfo.unitId)
        skillsList.forEachIndexed { index, element ->
            val skillsHis = mDbHelper.getSkillsHistoryDao().querySkillsHistory(element.unitId)
            val fragments = arrayListOf<Fragment>()
            skillsHis.forEachIndexed { indexHis, it ->
                val bundle = Bundle()
                bundle.putInt("id", it.id)
                if( indexHis + 1 < skillsHis.size){
                    bundle.putInt("lastId", skillsHis[indexHis + 1].id)
                }else{
                    bundle.putInt("lastId", -1)
                }
                val skillsFragment = SkillsFragment()
                skillsFragment.arguments = bundle
                fragments.add(skillsFragment)
            }
            when (index) {
                0 -> {
                    setViewPager(mRootView.skillsPager1, fragments)
                }
                1 -> {
                    setViewPager(mRootView.skillsPager2, fragments)
                }
                2 -> {
                    setViewPager(mRootView.skillsPager3, fragments)
                }
                3 -> {
                    setViewPager(mRootView.skillsPager4, fragments)
                }
            }
        }
    }


    private fun setViewPager(viewPager:ViewPager, fragments:ArrayList<Fragment>){
        viewPager.adapter = SkillsPagerAdapter(supportFragmentManager, fragments)
        viewPager.currentItem = 0
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
                    val retIntent = Intent(REC_DATA_SKILLS_IMAGE)
                    activity.sendBroadcast(retIntent)
                    Toast.makeText(activity, "update done", Toast.LENGTH_SHORT).show()
                }
                msg.what == 2 -> {
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, "update nothing", Toast.LENGTH_SHORT).show()
                }
            }
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

        @BindView(R.id.ll_tiers)
        lateinit var tiers:ListView

        @BindView(R.id.tv_type)
        lateinit var type:TextView

        @BindView(R.id.tv_lane)
        lateinit var lane:TextView

        @BindView(R.id.vp_basicinfo)
        lateinit var basicPager:ViewPager

        @BindView(R.id.vp_skills1)
        lateinit var skillsPager1:ViewPager

        @BindView(R.id.vp_skills2)
        lateinit var skillsPager2:ViewPager

        @BindView(R.id.vp_skills3)
        lateinit var skillsPager3:ViewPager

        @BindView(R.id.vp_skills4)
        lateinit var skillsPager4:ViewPager

        init {
            ButterKnife.bind(this, view)
        }

    }

}