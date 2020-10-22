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
import com.mx.cosmo.orm.vo.SaintInfo
import com.mx.cosmo.orm.vo.SkillsInfo
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL


class MainActivity: BaseActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private val handler = MyHandler(this)
    private val mDataList:MutableList<SaintInfo> = mutableListOf()
    private var mOrderBy:String = SaintInfo.ID
    private var mOderAsc:Boolean = true
    private val mAdapter = MainAdapter(this, mDataList)

    @BindView(R.id.lv_main)
    lateinit var listView:ListView

    @OnItemClick(R.id.lv_main)
    fun onListItemClick(position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("saint_id", mDataList[position].id)
        startActivity(intent)
    }

    @OnClick(R.id.header_profile, R.id.header_name, R.id.header_vit, R.id.header_Aura, R.id.header_tech)
    fun pickHeaderHandler(header:TextView) {
        var orderBy = ""
        when(header.id){
            R.id.header_profile -> orderBy = SaintInfo.ID
            R.id.header_name -> orderBy = SaintInfo.COLUMN_NAME
            R.id.header_vit -> orderBy = SaintInfo.COLUMN_RATE_VITALITY
            R.id.header_Aura -> orderBy = SaintInfo.COLUMN_RATE_AURA
            R.id.header_tech -> orderBy = SaintInfo.COLUMN_RATE_TECH
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
            val result:ArrayList<SaintInfo> = mDbHelper.getSaintInfoDao().querySaintList(mOrderBy, mOderAsc) as ArrayList<SaintInfo>
            val message = Message.obtain()
            message.what = 5
            val bundle = Bundle()
            bundle.putParcelableArrayList("result", result)
            message.data = bundle
            handler.sendMessage(message)
        }).start()
    }

    private fun updateList(result:List<SaintInfo>){
        if(result.isEmpty()){
            Thread(Runnable {
                val (saintData, skillsData) = Utils.loadJSONFromAsset(this, "seiya_data_20201007.json")
                saintData.forEach {
                    mDbHelper.getSaintInfoDao().createOrUpdateSaint(it)
                }
                skillsData.forEach {
                    mDbHelper.getSkillsInfoDao().createOrUpdateSkills(it)
                }
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
            R.id.action_update_saint -> {
                Thread(Runnable {
                    val (saintData, skillsData) = Utils.loadJSONFromAsset(this, "seiya_data_20201007.json")
                    saintData.forEach {
                        mDbHelper.getSaintInfoDao().createOrUpdateSaint(it)
                    }
                    skillsData.forEach {
                        mDbHelper.getSkillsInfoDao().createOrUpdateSkills(it)
                    }
                    handler.sendEmptyMessage(1)
                }).start()
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
                msg.what == 1 -> {
                    Toast.makeText(activity, "get record done", Toast.LENGTH_LONG).show()
                    activity.searchList()
                }
                msg.what == 5 -> { //search result
                    activity.mProgressDialog.dismiss()
                    val result:ArrayList<SaintInfo> = msg.data.getParcelableArrayList<SaintInfo>("result") as ArrayList<SaintInfo>
                    activity.updateList(result)
                }
            }
        }
    }

}