package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = SkillsHistory.TABLE_NAME)
class SkillsHistory {

    companion object{
        const val TABLE_NAME = "skill_history"
        const val ID = "_id"
        const val COLUMN_VERSION = "version" // database 2020-10-21
        const val COLUMN_LEVEL = "level"
        const val COLUMN_UNIT_ID = "unit_id" // 60305007
        const val COLUMN_NAME = "name"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_EFFECTS = "effects"
        const val COLUMN_IMAGE_ID = "image_id"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

    @DatabaseField(columnName = COLUMN_UNIT_ID)
    var unitId:Int = 0

    @DatabaseField
    var level:Int = 0

    @DatabaseField
    var name:String = ""

    @DatabaseField
    var version:String = ""

    @DatabaseField
    var description:String = ""

    @DatabaseField
    var effects:String = ""

    var image:ByteArray? = byteArrayOf()

    @DatabaseField(columnName = COLUMN_IMAGE_ID)
    var imageId:Int = 0

}