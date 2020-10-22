package com.mx.cosmo.orm.vo

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField

class ImageInfo constructor( @DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = COLUMN_IMAGE) var image:ByteArray = byteArrayOf()) {
    companion object{
        const val TABLE_NAME = "imageInfo"
        const val ID = "_id"
        const val COLUMN_IMAGE = "image"
    }

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, columnName = ID)
    var id:Int = 0
}