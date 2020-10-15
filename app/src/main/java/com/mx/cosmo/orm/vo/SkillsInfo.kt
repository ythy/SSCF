package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField

class SkillsInfo {

    companion object{
        const val TABLE_NAME = "skillsinfo"
        const val ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SAINT_ID = "saint_id"
        const val COLUMN_UNIT_ID = "unit_id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_EFFECTS = "effects"
        const val COLUMN_IMAGE = "image"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

    @DatabaseField(columnName = COLUMN_SAINT_ID)
    var saintId:Int = 0

    @DatabaseField(columnName = COLUMN_UNIT_ID)
    var unitId:Int = 0

    @DatabaseField
    var name:String = ""

    @DatabaseField
    var description:String = ""

    @DatabaseField
    var effects:String = ""

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    var image:ByteArray = byteArrayOf()

}