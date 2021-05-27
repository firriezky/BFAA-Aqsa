package com.aqsa.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.aqsa.myapplication.data.model.FollowResponse
import com.aqsa.myapplication.data.model.Resource
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.data.model.UserResponse
import com.aqsa.myapplication.util.URL
import com.google.gson.Gson

class SearchViewModel : ViewModel() {

    val responseUserList: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    val responseUserDetail: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    val responseFollow: MutableLiveData<Resource<FollowResponse>> = MutableLiveData()
    val gson = Gson()

    fun searchUser(username: String) {
        responseUserList.value = Resource.Loading()
        val url = "${URL.FIND_USER}$username"
        Log.d("response_fan", url)
        AndroidNetworking.get(url)
            .addHeaders("Authorization", URL.API_TOKEN)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    Log.d("response_fan", response.toString())
                    Log.d("response_fan", response.toString())
                    val model = gson.fromJson(response, SearchResponse::class.java)
                    if (model.totalCount > 0) {
                        responseUserList.value = Resource.Success(model)
                    } else {
                        responseUserList.value = Resource.Error("3")
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("response_fan", anError?.errorBody.toString())
                    Log.d("response_fan", anError?.errorDetail.toString())
                    responseUserList.value = Resource.Error("0")
                }

            })
    }


    fun detailUser(username: String) {
        responseUserDetail.value = Resource.Loading()
        val url = "${URL.DETAIL_USER}$username"
        Log.d("response_fan", url)
        AndroidNetworking.get(url)
            .addHeaders("Authorization", URL.API_TOKEN)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    Log.d("response_fan", response.toString())
                    Log.d("response_fan", response.toString())
                    val model = gson.fromJson(response, UserResponse::class.java)
                    responseUserDetail.value = Resource.Success(model)
                }

                override fun onError(anError: ANError?) {
                    Log.d("response_fan", anError?.errorBody.toString())
                    Log.d("response_fan", anError?.errorDetail.toString())
                    responseUserDetail.value = Resource.Error("0")

                }

            })
    }

    fun followDetail(url: String) {
        responseUserDetail.value = Resource.Loading()
        Log.d("response_fan", url)
        responseFollow.value = Resource.Loading()
        AndroidNetworking.get(url)
            .addHeaders("Authorization", URL.API_TOKEN)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    Log.d("response_fan", response.toString())
                    Log.d("response_fan", response.toString())
                    val model = gson.fromJson(response, FollowResponse::class.java)
                    responseFollow.value = Resource.Success(model)
                }

                override fun onError(anError: ANError?) {
                    Log.d("response_fan", anError?.errorBody.toString())
                    Log.d("response_fan", anError?.errorDetail.toString())
                    responseFollow.value = Resource.Error("0")
                }

            })
    }
}