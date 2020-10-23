package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@Suppress("unused")
@DatabaseTable(tableName = SaintHistory.TABLE_NAME)
class SaintHistory {

    companion object{
        const val TABLE_NAME = "saint_history"
        const val ID = "_id"
        const val COLUMN_SAINT_ID = "saint_id" //   10027701
        const val COLUMN_VERSION = "version" // database 2020-10-21
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
        const val COLUMN_PHYS_DEFENSE = "phys_defense"
        const val COLUMN_FURY_RESISTANCE = "fury_resistance"
        const val COLUMN_ACCURACY = "accuracy"
        const val COLUMN_EVASION = "evasion"
        const val COLUMN_HP_RECOVERY = "hp_recovery"
        const val COLUMN_COSMO_RECOVERY = "cosmo_recovery"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

    @DatabaseField(columnName = COLUMN_SAINT_ID)
    var saintId:Int = 0

    @DatabaseField(columnName = COLUMN_VERSION)
    var version:String = ""

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

    @DatabaseField(columnName = COLUMN_PHYS_DEFENSE)
    var physDefense:Int = 0

    @DatabaseField(columnName = COLUMN_FURY_RESISTANCE)
    var furyResistance:Int = 0

    @DatabaseField(columnName = COLUMN_ACCURACY)
    var accuracy:Int = 0

    @DatabaseField(columnName = COLUMN_EVASION)
    var evasion:Int = 0

    @DatabaseField(columnName = COLUMN_HP_RECOVERY)
    var recoveryHP:Int = 0

    @DatabaseField(columnName = COLUMN_COSMO_RECOVERY)
    var recoveryCosmo:Int = 0

}