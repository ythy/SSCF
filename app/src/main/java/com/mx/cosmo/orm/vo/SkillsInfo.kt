package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = SkillsInfo.TABLE_NAME)
class SkillsInfo {

    companion object{
        const val TABLE_NAME = "skill_info"
        const val ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SAINT_ID = "saint_id" // 305
        const val COLUMN_UNIT_ID = "unit_id" // 60305007
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_EFFECTS = "effects"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_IMAGE_ID = "image_id"
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

    var image:ByteArray? = byteArrayOf()

    @DatabaseField(columnName = COLUMN_IMAGE_ID)
    var imageId:Int = 0

}