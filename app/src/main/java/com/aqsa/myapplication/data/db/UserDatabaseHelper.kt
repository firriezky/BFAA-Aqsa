package com.aqsa.myapplication.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class UserDatabaseHelper(context : Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){


    companion object{
        private const val DB_NAME ="fav_user.db"
        private const val DB_VERSION = 1

        const val AUTHORITY = "com.aqsa.myapplication"
        const val SCHEME = "content"

        const val TABLE_FAVORITE_USER  ="user_favorite"
        const val KEY_ID = "_id"
        const val KEY_URL ="url"
        const val KEY_USERNAME ="username"
        const val KEY_PHOTO ="photo"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVORITE_USER)
            .build()

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_FAVORITE_USER (" +
                "$KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_URL TEXT," +
                "$KEY_USERNAME TEXT," +
                "$KEY_PHOTO TEXT" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE_USER")
    }


}