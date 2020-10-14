package com.mx.cosmo.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.*
import java.net.URL
import java.net.URLConnection
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.graphics.Bitmap.CompressFormat



/**
 * Created by maoxin on 2018/10/19.
 */
class FileUtils {

    companion object {

        @Throws(IOException::class)
        fun loadRemoteImage(image_url: URL): Bitmap {
            Log.e("loadRemoteImage", image_url.toString())
            val conn: URLConnection = image_url.openConnection()
            conn.connect()
            val bis = BufferedInputStream(conn.getInputStream())
            val bm: Bitmap = BitmapFactory.decodeStream(bis)
            bis.close()
            return bm
        }

        @Throws(IOException::class)
        fun loadRemoteImagesSize(image_url: URL): Long {
            val conn: URLConnection = image_url.openConnection()
            conn.connect()
            var total: Long = 0
            val bis = BufferedInputStream(conn.getInputStream())
            val buffer = ByteArray(1024)
            while (true) {
                val length = bis.read(buffer)
                if (length <= 0)
                    break
                total += length
            }
            bis.close()
            return total
        }

        @Throws(IOException::class)
        fun createFile(dir: String, filename: String): File {
            val fileDir = File(Environment.getExternalStorageDirectory(), dir)
            if (!fileDir.exists())
                fileDir.mkdirs()
            val result = File(fileDir.path, filename)
            if (!result.exists())
                result.createNewFile()
            return result
        }

        fun loadRemoteFile(url: String, dest: String) {
            val fileUrl = URL(url)
            val inputStream = fileUrl.openStream()
            val dis = DataInputStream(inputStream)
            val file: File = createFile(dest, getFileName(fileUrl.file))
            val fos = FileOutputStream(file)

            val buffer = ByteArray(1024)
            while (true) {
                val length = dis.read(buffer)
                if (length <= 0)
                    break
                fos.write(buffer, 0, length)
            }
        }

        @Throws(IOException::class)
        fun exportImgFromBitmap(bitmap: Bitmap, imageFile: File) {
            val bos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,
                    100, bos)
            bos.flush()
            bos.close()
        }

        fun getFileName(path: String): String {
            val reg = Regex("^(.+)/(.+)$")
            return path.replace(reg, "$2")
        }

        fun deleteDir(file: File) {
            val contents = file.listFiles()
            if (contents != null) {
                for (f in contents) {
                    deleteDir(f)
                }
            }
            file.delete()
        }

        //通过URI删除图片  避免缩略图不能及时删除问题
        fun deleteImages(context: Context, file: File) {
            val uri = Uri.fromFile(file)
            if (uri.scheme == "file") {
                var path: String? = uri.encodedPath
                if (path != null) {
                    path = Uri.decode(path)
                    val cr = context.contentResolver
                    val buff = StringBuffer()
                    buff.append("(")
                            .append(MediaStore.Images.ImageColumns.DATA)
                            .append("=")
                            .append("'$path'")
                            .append(")")
                    val cur = cr.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            arrayOf(MediaStore.Images.ImageColumns._ID),
                            buff.toString(), null, null)
                    var index = 0
                    cur!!.moveToFirst()
                    while (!cur.isAfterLast) {
                        index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                        index = cur.getInt(index)
                        cur.moveToNext()
                    }
                    cur.close()

                    if (index != 0) {
                        context.contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.ImageColumns._ID + "=" + index, null)
                    } else
                        deleteImage(context, file)
                }
            }
        }

        private fun deleteImage(context: Context, fdelete: File) {
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Log.e("-->", "file Deleted :" + fdelete.path)
                    callBroadCast(context, fdelete)
                } else {
                    Log.e("-->", "file not Deleted :" + fdelete.path)
                }
            }
        }

        private fun callBroadCast(context: Context, file: File) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
                Log.e("-->", " >= 19")
                val mediaScanIntent = Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val contentUri = Uri.fromFile(file) //out is your output file
                mediaScanIntent.data = contentUri
                context.sendBroadcast(mediaScanIntent)
            } else {
                Log.e("-->", " < 19")
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())))
            }
        }

        fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, 100, outputStream)
            return outputStream.toByteArray()
        }

    }
}