package com.aqsa.myapplication.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.db.UserDatabaseManager
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.databinding.ItemUserBinding
import com.aqsa.myapplication.util.Util.makeLongToast
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import de.hdodenhof.circleimageview.CircleImageView

class UserSearchAdapter(private val mContext: Context) :
    RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {
    lateinit var searchInterface: UserSearchInterface
    private val data = mutableListOf<SearchResponse.Item>()


    fun setData(newData: MutableList<SearchResponse.Item>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private val db: UserDatabaseManager by lazy {
        UserDatabaseManager(mContext)
    }

    fun setInterface(newInterface: UserSearchInterface) {
        this.searchInterface = newInterface
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class UserViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val vbind = ItemUserBinding.bind(v)
        fun bind(model: SearchResponse.Item) {
            vbind.root.setOnClickListener {
                searchInterface.onUserClick(model)
            }
            vbind.username.text = model.login

            loadGlide(model.avatarUrl, vbind.pict)
            val isAlreadyLiked = db.checkIfAlreadyFavorited(model.url)
            vbind.like.isLiked = isAlreadyLiked

            vbind.like.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    if (!isAlreadyLiked) {
                        model.apply {
                            db.saveData(
                                model.login,
                                model.avatarUrl,
                                model.url
                            )
                        }
                        mContext.makeLongToast("${model.login} ${mContext.getString(R.string.success_add)}")
                    }
                }

                override fun unLiked(likeButton: LikeButton?) {
                    db.deleteByUsername(model.login)
                    mContext.makeLongToast("${model.login} ${mContext.getString(R.string.success_remove)}")

                }
            })
        }


        private fun loadGlide(avatarUrl: String, pict: CircleImageView) {
            Glide
                .with(vbind.root)
                .load(avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_loading)
                .thumbnail(Glide.with(vbind.root).load(R.raw.loading_animation))
                .dontAnimate()
                .into(vbind.pict)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    interface UserSearchInterface {
        fun onUserClick(user: SearchResponse.Item)
    }
}