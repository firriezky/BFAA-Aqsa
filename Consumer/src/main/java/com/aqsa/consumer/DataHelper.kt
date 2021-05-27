package com.aqsa.consumer

import android.net.Uri

object DataHelper {
    const val AUTHORITY = "com.aqsa.myapplication"
    const val SCHEME = "content"

    const val TABLE_FAV = "user_favorite"
    const val KEY_URL = "url"
    const val KEY_USERNAME = "username"
    const val KEY_PHOTO = "photo"

    // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABLE_FAV)
        .build()
}