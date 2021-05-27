package com.aqsa.myapplication.util

import com.aqsa.myapplication.BuildConfig

object URL {
    const val BASE_URL = BuildConfig.BaseUrl
    const val API_TOKEN = BuildConfig.ApiKey
    const val FIND_USER = "$BASE_URL/search/users?q="
    const val DETAIL_USER = "$BASE_URL/users/"
}