package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@Suppress("unused")
@DatabaseTable(tableName = Version.TABLE_NAME)
class Version constructor(@DatabaseField var version: String = "",
                          @DatabaseField var date: String = "" ) {

    companion object{
        const val TABLE_NAME = "version_info"
        const val ID = "_id"
        const val COLUMN_VERSION = "version"
        const val COLUMN_DATE= "date"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0

}