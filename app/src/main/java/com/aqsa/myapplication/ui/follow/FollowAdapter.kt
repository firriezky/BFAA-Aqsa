package com.aqsa.myapplication.ui.follow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.db.UserDatabaseManager
import com.aqsa.myapplication.data.model.FollowResponse
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.databinding.ItemUserBinding
import com.aqsa.myapplication.ui.home.UserSearchAdapter
import com.aqsa.myapplication.util.Util.makeLongToast
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import de.hdodenhof.circleimageview.CircleImageView

class FollowAdapter(private val mContext: Context) :
    RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {
    lateinit var followInterface: FollowInterface
    private val data = mutableListOf<FollowResponse.FollowResponseItem>()


    fun setData(newData: MutableList<FollowResponse.FollowResponseItem>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private val db: UserDatabaseManager by lazy {
        UserDatabaseManager(mContext)
    }

    fun setInterface(newInterface: FollowInterface) {
        this.followInterface = newInterface
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class FollowViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val vbind = ItemUserBinding.bind(v)
        fun bind(model: FollowResponse.FollowResponseItem) {
            vbind.root.setOnClickListener {
                followInterface.onUserClick(model)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FollowViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    interface FollowInterface {
        fun onUserClick(user:FollowResponse.FollowResponseItem)
    }
}