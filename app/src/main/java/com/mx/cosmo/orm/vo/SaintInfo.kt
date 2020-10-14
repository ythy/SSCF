package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField

class SaintInfo {

    companion object{
        const val TABLE_NAME = "saintinfo"
        const val ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_UNIT_ID = "unit_id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_LANE = "lane"
        const val COLUMN_ACTIVE_TIME = "active_time"
        const val COLUMN_TIERS_PVP = "tiers_PVP"
        const val COLUMN_TIERS_CRUSADE = "tiers_crusade"
        const val COLUMN_TIERS_PVE = "tiers_PVE"
        const val COLUMN_POWER = "power"
        const val COLUMN_RATE_VITALITY = "grow_rate_vitality"
        const val COLUMN_RATE_AURA = "grow_rate_aura"
        const val COLUMN_RATE_TECH = "grow_rate_tech"
        const val COLUMN_VITALITY = "vitality"
        const val COLUMN_AURA = "aura"
        const val COLUMN_TECH = "technique"
        const val COLUMN_HP = "hp"
        const val COLUMN_PHYS_ATTACK = "phys_attack"
        const val COLUMN_FURY_ATTACK = "fury_attack"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

    @DatabaseField
    var name:String = ""

    @DatabaseField(columnName = COLUMN_UNIT_ID)
    var unitId:Int = 0

    @DatabaseField
    var type:String = ""

    @DatabaseField
    var lane:String = ""

    @DatabaseField(columnName = COLUMN_ACTIVE_TIME)
    var activeTime:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_PVP)
    var tiersPVP:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_CRUSADE)
    var tiersCrusade:String = ""

    @DatabaseField(columnName = COLUMN_TIERS_PVE)
    var tiersPVE:String = ""

    @DatabaseField
    var power:Int = 0

    @DatabaseField(columnName = COLUMN_RATE_VITALITY)
    var vitalityRate: Double = 0.0

    @DatabaseField(columnName = COLUMN_RATE_AURA)
    var auraRate: Double = 0.0

    @DatabaseField(columnName = COLUMN_RATE_TECH)
    var techRate: Double = 0.0

    @DatabaseField
    var vitality:Int = 0

    @DatabaseField
    var aura:Int = 0

    @DatabaseField
    var technique:Int = 0

    @DatabaseField
    var hp:Int = 0

    @DatabaseField(columnName = COLUMN_PHYS_ATTACK)
    var physAttack:Int = 0

    @DatabaseField(columnName = COLUMN_FURY_ATTACK)
    var furyAttack:Int = 0

}