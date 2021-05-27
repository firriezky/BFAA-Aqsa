package com.aqsa.myapplication.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.KEY_ID
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.KEY_PHOTO
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.KEY_URL
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.KEY_USERNAME
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.TABLE_FAVORITE_USER
import com.aqsa.myapplication.data.model.FavoriteModel

class UserDatabaseManager(context: Context) {

    private val db by lazy {
        UserDatabaseHelper(context)
    }

    fun getWritableDatabase() : SQLiteDatabase{
        return db.writableDatabase
    }

    fun deleteByUsername(username: String): Int {
        val writeableDb = db.writableDatabase
        return writeableDb.delete(TABLE_FAVORITE_USER, "$KEY_USERNAME = '$username'", null)
    }

    fun deleteByID(id: String): Int {
        val writeableDb = db.writableDatabase
        return writeableDb.delete(TABLE_FAVORITE_USER, "$KEY_ID = '$id'", null)
    }

    fun saveData(username:String,photo:String,url:String){
        val writeableDb = db.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_USERNAME,username)
        contentValues.put(KEY_PHOTO,photo)
        contentValues.put(KEY_URL,url)

        writeableDb.insert(
            TABLE_FAVORITE_USER,null,contentValues
        )

        writeableDb.close()

    }

    fun saveDataWithContentValue(contentValues: ContentValues?)  : Long{
        val writeableDb = db.writableDatabase

        return writeableDb.insert(
            TABLE_FAVORITE_USER,null,contentValues
        )

    }

    fun checkIfAlreadyFavorited(url : String) : Boolean{
        val readableDb = db.readableDatabase
        val cursor = readableDb.rawQuery(
            "SELECT * FROM ${TABLE_FAVORITE_USER} WHERE ${KEY_URL} = '${url}'",
            null
        )
        return cursor.count != 0
    }

    fun queryAll() : Cursor{
        val readableDb = db.readableDatabase
        val cursor = readableDb.rawQuery(
            "SELECT * FROM ${TABLE_FAVORITE_USER}",
            null
        )
        return cursor
    }

    fun queryById(id : String) : Cursor{
        val readableDb = db.readableDatabase
        val cursor = readableDb.rawQuery(
            "SELECT * FROM ${TABLE_FAVORITE_USER} WHERE $KEY_ID = '$id' ",
            null
        )
        return cursor
    }

    fun getData(): List<FavoriteModel> {
        val favList: MutableList<FavoriteModel> = mutableListOf()

        val readableDb = db.readableDatabase

        val cursor = readableDb.rawQuery(
            "SELECT * FROM ${TABLE_FAVORITE_USER}",
            null
        )

        cursor?.apply {
            while (moveToNext()) {
                val fav =
                    FavoriteModel(
                        url = getString(getColumnIndexOrThrow(KEY_URL)),
                        photo = getString(getColumnIndexOrThrow(KEY_PHOTO)),
                        username = getString(getColumnIndexOrThrow(KEY_USERNAME))
                    )
                favList.add(fav)
            }
        }

        return favList
    }


}