package com.mx.cosmo.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.activity.DetailActivity
import com.mx.cosmo.common.FloatTextView
import com.mx.cosmo.orm.vo.SkillsHistory


/**
 * Created by maoxin on 2022/04/12.
 */
class SkillsFragment : Fragment() {

    companion object{
        const val REC_DATA_SKILLS_IMAGE = "update_skills_image"
    }

    lateinit var context: DetailActivity
    private lateinit var mView:View
    private lateinit var skillsView: Skills
    private lateinit var mDataReceiver: DataReceiver
    private var mId: Int = 0
    private var mLastId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_skills, container, false)
        context = activity as DetailActivity
        this.skillsView = Skills(mView)
        mId = arguments!!.getInt("id")
        mLastId = arguments!!.getInt("lastId")
        this.setSkill()
        mDataReceiver = DataReceiver()
        context.registerReceiver(mDataReceiver, IntentFilter(REC_DATA_SKILLS_IMAGE))
        return mView
    }

    override fun onDetach() {
        super.onDetach()
        context.unregisterReceiver(mDataReceiver)
    }

    private fun setSkill(){
        val skillsHistory = context.mDbHelper.getSkillsHistoryDao().querySkillsHistorContainsImage(mId)
        var lastInfo:SkillsHistory? = null
        if(mLastId > 0){
            lastInfo = context.mDbHelper.getSkillsHistoryDao().queryForId(mLastId)
        }
        skillsView.version.text = skillsHistory.version
        skillsView.name.text = skillsHistory.name
        if(skillsHistory.image != null && skillsHistory.image!!.isNotEmpty())
            skillsView.image.setImageBitmap(BitmapFactory.decodeByteArray(skillsHistory.image, 0, skillsHistory.image!!.size))
        else{
            skillsView.image.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher))
        }

        skillsView.descriptionFloat.text = skillsHistory.description
        skillsView.descriptionFloat.overflowTextViewId = R.id.overflowTextView
        if(lastInfo?.description != null && skillsHistory.description != lastInfo.description){
            skillsView.descriptionFloat.setTextColor(resources.getColor(R.color.colorDiff, null))

        }
        if(lastInfo?.effects != null && skillsHistory.effects != lastInfo.effects){
            val level = skillsHistory.level.toString()
            val spannable = SpannableString(skillsHistory.effects)
            val effectArray = skillsHistory.effects.split(" ")
            var index = 0
            effectArray.forEachIndexed { i, it ->
                if(it.trim() == level){
                    spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorDiff, null)), index, index + it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }else if(it.trim().indexOf("@$level") == 0 ){
                    spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorDiff, null)), index, index + "@$level".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    if(i - 1 >= 0){
                        val lastLength = effectArray[i - 1].length
                        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorDiff, null)), index - 1 - lastLength, index - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                index += it.length + 1
            }
            skillsView.effects.setText(spannable, TextView.BufferType.SPANNABLE)
        }else{
            skillsView.effects.text = skillsHistory.effects
        }
    }

    fun updateImage(){
        val skillsHistory = context.mDbHelper.getSkillsHistoryDao().querySkillsHistorContainsImage(mId)
        if(skillsHistory.image != null && skillsHistory.image!!.isNotEmpty())
            skillsView.image.setImageBitmap(BitmapFactory.decodeByteArray(skillsHistory.image, 0, skillsHistory.image!!.size))
        else{
            skillsView.image.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher))
        }
    }

    class Skills constructor(view: View){

        @BindView(R.id.tv_skills_name)
        lateinit var name: TextView

        @BindView(R.id.tv_skill_effect)
        lateinit var effects: TextView

        @BindView(R.id.tv_version)
        lateinit var version: TextView

        @BindView(R.id.tv_skill_description_float)
        lateinit var descriptionFloat: FloatTextView

        @BindView(R.id.imageView)
        lateinit var image: ImageView


        init {
            ButterKnife.bind(this, view)
        }
    }

    private inner class DataReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            updateImage()
        }
    }

}