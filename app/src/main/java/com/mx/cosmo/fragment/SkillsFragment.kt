package com.mx.cosmo.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.activity.DetailActivity
import com.mx.cosmo.orm.vo.SkillsHistory


/**
 * Created by maoxin on 2022/04/12.
 */
class SkillsFragment : Fragment() {

    lateinit var context: DetailActivity
    private lateinit var skillsView: Skills

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_skills, container, false)
        context = activity as DetailActivity
        this.skillsView = Skills(view)
        val id = arguments!!.getInt("id")
        val lastId = arguments!!.getInt("lastId")
        this.setSkill(id, lastId)
        return view
    }

    private fun setSkill(id:Int, lastId:Int){
        val skillsHistory = context.mDbHelper.getSkillsHistoryDao().queryForId(id)
        var lastInfo:SkillsHistory? = null
        if(lastId > 0){
            lastInfo = context.mDbHelper.getSkillsHistoryDao().queryForId(lastId)
        }
        skillsView.version.text = skillsHistory.version
        skillsView.name.text = skillsHistory.name

        skillsView.description.text = skillsHistory.description
        if(lastInfo?.description != null && skillsHistory.description != lastInfo.description){
            skillsView.description.setTextColor(Color.rgb(215,81,81))
        }
        if(lastInfo?.effects != null){
            val level = skillsHistory.level.toString()
            val spannable = SpannableString(skillsHistory.effects)
            val effectArray = skillsHistory.effects.split(" ")
            var index = 0
            effectArray.forEachIndexed { i, it ->
                if(it.trim() == level){
                    spannable.setSpan(ForegroundColorSpan(Color.rgb(215,81,81)), index, index + it.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }else if(it.trim().indexOf("@$level") == 0 ){
                    spannable.setSpan(ForegroundColorSpan(Color.rgb(215,81,81)), index, index + "@$level".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    if(i - 1 >= 0){
                        val lastLength = effectArray[i - 1].length
                        spannable.setSpan(ForegroundColorSpan(Color.rgb(215,81,81)), index - 1 - lastLength, index - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                index += it.length + 1
            }
            skillsView.effects.setText(spannable, TextView.BufferType.SPANNABLE)
        }else{
            skillsView.effects.text = skillsHistory.effects
        }

    }

    class Skills constructor(view: View){

        @BindView(R.id.tv_skills_name)
        lateinit var name: TextView

        @BindView(R.id.tv_skill_description)
        lateinit var description: TextView

        @BindView(R.id.tv_skill_effect)
        lateinit var effects: TextView

        @BindView(R.id.tv_version)
        lateinit var version: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

}