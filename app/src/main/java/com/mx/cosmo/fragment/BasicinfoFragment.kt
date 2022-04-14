package com.mx.cosmo.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mx.cosmo.R
import com.mx.cosmo.activity.DetailActivity
import com.mx.cosmo.orm.vo.SaintHistory

/**
* Created by maoxin on 2022/04/12.
*/
class BasicinfoFragment : Fragment() {

    lateinit var context: DetailActivity
    private lateinit var mRootView: RootView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_basicinfo, container, false)
        context = activity as DetailActivity
        this.mRootView = RootView(view)
        val id = arguments!!.getInt("id")
        val lastId = arguments!!.getInt("lastId")
        this.setBaseView(id, lastId)
        return view
    }

    private fun setBaseView(id:Int, lastId:Int){
        val detailInfo:SaintHistory = context.mDbHelper.getSaintHistoryDao().queryForId(id)
        var lastInfo:SaintHistory? = null
        if(lastId > 0){
            lastInfo = context.mDbHelper.getSaintHistoryDao().queryForId(lastId)
        }
        mRootView.version.text = detailInfo.version
        setTextWithColor(mRootView.power, detailInfo.power, lastInfo?.power)
        setTextWithColor(mRootView.rateVit, detailInfo.vitalityRate, lastInfo?.vitalityRate)
        setTextWithColor(mRootView.rateAura, detailInfo.auraRate, lastInfo?.auraRate)
        setTextWithColor(mRootView.rateTech, detailInfo.techRate, lastInfo?.techRate)
        setTextWithColor(mRootView.vitality, detailInfo.vitality, lastInfo?.vitality)
        setTextWithColor(mRootView.aura, detailInfo.aura, lastInfo?.aura)
        setTextWithColor(mRootView.tech, detailInfo.technique, lastInfo?.technique)
        setTextWithColor(mRootView.hp, detailInfo.hp, lastInfo?.hp)
        setTextWithColor(mRootView.physAttack, detailInfo.physAttack, lastInfo?.physAttack)
        setTextWithColor(mRootView.furyAttack, detailInfo.furyAttack, lastInfo?.furyAttack)
        setTextWithColor(mRootView.physDefense, detailInfo.physDefense, lastInfo?.physDefense)
        setTextWithColor(mRootView.furyResistance, detailInfo.furyResistance, lastInfo?.furyResistance)
        setTextWithColor(mRootView.accuracy, detailInfo.accuracy, lastInfo?.accuracy)
        setTextWithColor(mRootView.evasion, detailInfo.evasion, lastInfo?.evasion)
        setTextWithColor(mRootView.hpRecovery, detailInfo.recoveryHP, lastInfo?.recoveryHP)
        setTextWithColor(mRootView.cosmoRecovery, detailInfo.recoveryCosmo, lastInfo?.recoveryCosmo)
    }

    private fun setTextWithColor(view:TextView, newText:Double, oldText:Double? = null ){
        view.text = newText.toString()
        if(oldText != null && newText != oldText){
            view.setTextColor(Color.rgb(215,81,81))
        }
    }

    private fun setTextWithColor(view:TextView, newText:Int, oldText:Int? = null ){
        view.text = newText.toString()
        if(oldText != null && newText != oldText){
            view.setTextColor(Color.rgb(215,81,81))
        }
    }

    class RootView constructor(view: View){

        @BindView(R.id.tv_power)
        lateinit var power: TextView

        @BindView(R.id.tv_rate_vit)
        lateinit var rateVit: TextView

        @BindView(R.id.tv_rate_aura)
        lateinit var rateAura: TextView

        @BindView(R.id.tv_rate_tech)
        lateinit var rateTech: TextView

        @BindView(R.id.tv_vitality)
        lateinit var vitality: TextView

        @BindView(R.id.tv_aura)
        lateinit var aura: TextView

        @BindView(R.id.tv_tech)
        lateinit var tech: TextView

        @BindView(R.id.tv_hp)
        lateinit var hp: TextView

        @BindView(R.id.tv_phys_attack)
        lateinit var physAttack: TextView

        @BindView(R.id.tv_fury_attack)
        lateinit var furyAttack: TextView

        @BindView(R.id.tv_phys_defense)
        lateinit var physDefense: TextView

        @BindView(R.id.tv_fury_resistance)
        lateinit var furyResistance: TextView

        @BindView(R.id.tv_accuracy)
        lateinit var accuracy: TextView

        @BindView(R.id.tv_evasion)
        lateinit var evasion: TextView

        @BindView(R.id.tv_hp_recovery)
        lateinit var hpRecovery: TextView

        @BindView(R.id.tv_cosmo_recovery)
        lateinit var cosmoRecovery: TextView

        @BindView(R.id.tv_version)
        lateinit var version: TextView


        init {
            ButterKnife.bind(this, view)
        }

    }
}


