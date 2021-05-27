package com.aqsa.consumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aqsa.consumer.DataHelper.CONTENT_URI
import com.aqsa.consumer.DataHelper.KEY_PHOTO
import com.aqsa.consumer.DataHelper.KEY_URL
import com.aqsa.consumer.DataHelper.KEY_USERNAME
import com.aqsa.consumer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val listUsers = mutableListOf<FavoriteModel>()
    val adapterConsumer by lazy { UserFavoriteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterConsumer
        }

        adapterConsumer.setInterface(object : UserFavoriteAdapter.FavoriteAdapterInterface {
            override fun onShareClick(user: FavoriteModel) {
                val text = ShareCompat.IntentBuilder.from(this@MainActivity)
                    .setType("text/plain")
                    .setText("""
                      GitHub, Inc. is a provider of Internet hosting for software development and version control using Git. It offers the distributed version control and source code management functionality of Git, plus its own features
                      ${user.url} is using github. let see him on git !.
                    """.trimIndent())
                    .intent
                if (text.resolveActivity(packageManager) != null) {
                    startActivity(text)
                }
            }
        })

        val contentProviderClient = contentResolver.acquireContentProviderClient(CONTENT_URI)
        try {
            if (contentProviderClient == null) {
                Log.d("contentStat", "error apa ya")
            } else {
                Log.d("contentStat", contentProviderClient.toString())
                Log.d("contentStat", "nggak ada error sih ini")
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                Log.d("cursorCount", cursor?.count.toString())
                Log.d("cursorCount1", cursor?.getColumnName(0).toString())
                Log.d("cursorCount2", cursor?.getColumnName(1).toString())
                Log.d("cursorCount3", cursor?.getColumnName(2).toString())
                Log.d("cursorCount", cursor?.count.toString())

                cursor?.apply {
                    while (moveToNext()) {
                        val fav =
                            FavoriteModel(
                                url = getString(getColumnIndexOrThrow(KEY_URL)),
                                photo = getString(getColumnIndexOrThrow(KEY_PHOTO)),
                                username = getString(getColumnIndexOrThrow(KEY_USERNAME)),
                            )
                        listUsers.add(fav)
                    }
                }
                adapterConsumer.setData(listUsers)
                adapterConsumer.notifyDataSetChanged()

            }
        } catch (remoteException: RemoteException) {
            Log.e("Cursor_Error", remoteException.toString())
        }
    }
}