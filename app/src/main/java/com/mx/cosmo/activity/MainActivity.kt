package com.mx.cosmo.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemClick
import com.mx.cosmo.R
import com.mx.cosmo.adapter.MainAdapter
import com.mx.cosmo.common.FileUtils
import com.mx.cosmo.common.Setting
import com.mx.cosmo.common.Utils
import com.mx.cosmo.orm.vo.*
import java.io.IOException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URL


class MainActivity: BaseActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private val handler = MyHandler(this)
    private val mDataList:MutableList<SaintInfo> = mutableListOf()
    private var mOrderBy:String = SaintInfo.ID
    private var mOderAsc:Boolean = false
    private val mAdapter = MainAdapter(this, mDataList)

    @BindView(R.id.lv_main)
    lateinit var listView:ListView

    @OnItemClick(R.id.lv_main)
    fun onListItemClick(position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("saint_id", mDataList[position].id)
        startActivity(intent)
    }

    @OnClick(R.id.header_profile, R.id.header_name, R.id.header_vit, R.id.header_Aura,
        R.id.header_tech, R.id.header_pvp, R.id.header_time)
    fun pickHeaderHandler(header:TextView) {
        var orderBy = ""
        when(header.id){
            R.id.header_profile -> orderBy = SaintInfo.ID
            R.id.header_name -> orderBy = SaintInfo.COLUMN_NAME
            R.id.header_vit -> orderBy = SaintHistory.COLUMN_RATE_VITALITY
            R.id.header_Aura -> orderBy = SaintHistory.COLUMN_RATE_AURA
            R.id.header_tech -> orderBy = SaintHistory.COLUMN_RATE_TECH
            R.id.header_pvp -> orderBy = TierInfo.COLUMN_TIERS_PVP
            R.id.header_time -> orderBy = SaintInfo.COLUMN_ACTIVE_TIME
        }
        mOderAsc = if( orderBy == mOrderBy ) !mOderAsc else false
        mOrderBy = orderBy
        searchList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        ButterKnife.bind(this)
        requestPermission()
    }


    private fun requestPermission() {
        if (!hasPermission()) {
            requestPermissions(permissions, MY_PERMISSIONS_REQUEST)
        } else
            searchList()
    }

    private fun hasPermission(): Boolean {
        var result = true
        permissions.forEach {
            val res = checkCallingOrSelfPermission(it)
            if (res != PackageManager.PERMISSION_GRANTED)
                result = false
        }
        return result
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchList()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    private fun searchList(){
        mProgressDialog.show()
        Thread(Runnable {
            val result:ArrayList<SaintInfo> = mDbHelper.getSaintInfoDao().getSaintList(mOrderBy, mOderAsc) as ArrayList<SaintInfo>
            val version = mDbHelper.getVersionDao().getLastVersion()?.date
            val message = Message.obtain()
            message.what = 5
            val bundle = Bundle()
            bundle.putParcelableArrayList("result", result)
            bundle.putString("version", version)
            message.data = bundle
            handler.sendMessage(message)
        }).start()
    }

    fun updateDB(saintData:ArrayList<SaintInfo>, skillsData:ArrayList<SkillsInfo>, versionDate:String, versionNumber:String = ""){
        saintData.forEach {
            mDbHelper.getSaintInfoDao().createNewSaint(it)
            mDbHelper.getSaintHistoryDao().createSaintDetail(it.detailInfo)
            mDbHelper.getTierInfoDao().createTier(it.detailTier)
        }
        skillsData.forEach {
            mDbHelper.getSkillsInfoDao().createNewSkills(it)
            mDbHelper.getSkillsHistoryDao().createSkillsDetail(it.details)
        }
        val version = Version()
        version.date = versionDate
        version.version = versionNumber
        mDbHelper.getVersionDao().create(version)
    }

    private fun updateList(result:List<SaintInfo>){
        if(result.isEmpty()){
            mProgressDialog.show()
            Thread(Runnable {
                val (saintData, skillsData) = Utils.loadJSONFromAsset(this, Setting.DEFAULT_DATA_FILE)
                updateDB(saintData, skillsData, Setting.DEFAULT_DATA_DATE, Setting.DEFAULT_DATA_VERSION)
                handler.sendEmptyMessage(1)
            }).start()
        }else{
            mDataList.clear()
            mDataList.addAll(result)
            listView.adapter = mAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // action with ID action_refresh was selected
            R.id.action_get_last_version_data -> {
                mProgressDialog.show()
                Thread(Runnable {
                    val oldVersion = mDbHelper.getVersionDao().getLastVersion()?.date ?: ""
                    val msg = Message.obtain()
                    try {
                        val jsonObject = Utils.loadRemoteJsonData(Setting.VERSION_REMOTE_URL)
                        val newVersion = jsonObject.getString("date")
                        val newVersionNumber = jsonObject.getString("version")
                        if(newVersion > oldVersion){
                            val json = Utils.loadRemoteJsonData(Setting.SAINT_REMOTE_URL)
                            val (saintData, skillsData) = Utils.parseJsonData(json, newVersion)
                            updateDB(saintData, skillsData, newVersion, newVersionNumber)
                            msg.what = 3
                            msg.obj = "更新完成"
                        }else{
                            msg.what = 2
                            msg.obj = "没有最新版本"
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        msg.what = 2
                        msg.obj = e.message
                    }finally {
                        handler.sendMessage(msg)
                    }
                }).start()
            }
            R.id.action_get_last_version -> {
                mProgressDialog.show()
                Thread(Runnable {
                    val msg = Message.obtain()
                    msg.what = 6
                    try {
                        val jsonObject = Utils.loadRemoteJsonData(Setting.VERSION_REMOTE_URL)
                        val version = jsonObject.getString("date")
                        msg.obj = version
                    }catch (e:Exception){
                        e.printStackTrace()
                        msg.obj = e.message
                    }finally {
                        handler.sendMessage(msg)
                    }
                }).start()
            }
            R.id.action_get_small_images -> {
                mProgressDialog.show()
                Thread(Runnable {
                    this.mDataList.forEach {
                        val url = URL(Setting.RESOURCES_REMOTE_URL + "${it.unitId}_0.png")
                        try {
                            if(it.imageSmallId <= 0) {
                                val bmp = FileUtils.loadRemoteImage(url)
                                val imageInfo = mDbHelper.getImageInfoDao()
                                    .createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                                mDbHelper.getSaintInfoDao().updateSaintSmallImage(it.id, imageInfo.id)
                            }
                        }catch (e:IOException){
                            Log.e(TAG, e.message)
                        }
                    }
                    handler.sendEmptyMessage(7)
                }).start()
            }
            R.id.action_get_full_images -> {
                mProgressDialog.show()
                Thread(Runnable {
                    this.mDataList.forEach {
                        val url = URL(Setting.RESOURCES_REMOTE_URL + "${it.unitId}.png")
                        try {
                            if(it.imageFullId <= 0) {
                                val bmp = FileUtils.loadRemoteImage(url)
                                val imageInfo = mDbHelper.getImageInfoDao()
                                    .createIfNotExists(ImageInfo(FileUtils.getBitmapAsByteArray(bmp)))
                                mDbHelper.getSaintInfoDao().updateSaintFullImage(it.id, imageInfo.id)
                            }
                        }catch (e:IOException){
                            Log.e(TAG, e.message)
                        }
                    }
                    handler.sendEmptyMessage(7)
                }).start()
            }
            R.id.action_get_version_20220408-> {
                mProgressDialog.show()
                val date = "2022-04-08"
                Thread(Runnable {
                    val (saintData, skillsData) =
                        Utils.loadJSONFromAsset(this, Setting.DEFAULT_UPGRADE_DATA_FILE, date)
                    updateDB(saintData, skillsData, date)
                    handler.sendEmptyMessage(1)
                }).start()
            }
            R.id.action_h5-> {
                val intent = Intent(this, WebActivity::class.java)
                startActivity(intent)
            }
            else -> {

            }
        }
        return true
    }

    class MyHandler(activity: MainActivity) : Handler() {

        private val outer: WeakReference<MainActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = outer.get()!!
            when {
                msg.what == 1 -> { //程序初始化
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, "get record done", Toast.LENGTH_LONG).show()
                    activity.searchList()
                }
                msg.what == 2 -> { //更新数据失败
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show()
                }
                msg.what == 3 -> { //更新数据成功
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show()
                    activity.searchList()
                }
                msg.what == 5 -> { //search result
                    activity.mProgressDialog.dismiss()
                    val result:ArrayList<SaintInfo> = msg.data.getParcelableArrayList<SaintInfo>("result") as ArrayList<SaintInfo>
                    activity.supportActionBar!!.title = "SSCF  " + ( msg.data.getString("version") ?: "" )
                    activity.updateList(result)
                }
                msg.what == 6 -> { //查最新版本
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show()
                }
                msg.what == 7 -> {
                    activity.mProgressDialog.dismiss()
                    Toast.makeText(activity, "图片获取成功!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}