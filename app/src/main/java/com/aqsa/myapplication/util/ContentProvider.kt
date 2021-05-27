package com.aqsa.myapplication.util

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.AUTHORITY
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.CONTENT_URI
import com.aqsa.myapplication.data.db.UserDatabaseHelper.Companion.TABLE_FAVORITE_USER
import com.aqsa.myapplication.data.db.UserDatabaseManager

class ContentProvider : ContentProvider() {

    private val db: UserDatabaseManager by lazy { UserDatabaseManager(context!!) }

    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var sqlite: SQLiteDatabase
    }

    init {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_USER, USER)
        sUriMatcher.addURI(AUTHORITY, "$TABLE_FAVORITE_USER/#", USER_ID)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Only Read, so this method remain empty in my app")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> db.saveDataWithContentValue(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        sqlite = db.getWritableDatabase()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> db.queryAll()
            USER_ID -> db.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Only Read, so this method remain empty in my app")
    }
}