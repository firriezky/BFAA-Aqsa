package com.aqsa.myapplication.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aqsa.myapplication.R
import com.aqsa.myapplication.data.db.UserDatabaseManager
import com.aqsa.myapplication.data.model.FavoriteModel
import com.aqsa.myapplication.data.model.FollowResponse
import com.aqsa.myapplication.data.model.SearchResponse
import com.aqsa.myapplication.databinding.ItemUserBinding
import com.aqsa.myapplication.ui.home.UserSearchAdapter
import com.aqsa.myapplication.util.Util.makeLongToast
import com.bumptech.glide.Glide
import com.like.LikeButton
import com.like.OnLikeListener
import de.hdodenhof.circleimageview.CircleImageView

class UserFavoriteAdapter(private val mContext: Context) :
    RecyclerView.Adapter<UserFavoriteAdapter.FollowViewHolder>() {
    lateinit var favInterface: FavoriteAdapterInterface
    private val data = mutableListOf<FavoriteModel>()


    fun setData(newData: MutableList<FavoriteModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private val db: UserDatabaseManager by lazy {
        UserDatabaseManager(mContext)
    }

    fun setInterface(newInterface: FavoriteAdapterInterface) {
        this.favInterface = newInterface
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class FollowViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val vbind = ItemUserBinding.bind(v)
        fun bind(model: FavoriteModel) {
            vbind.root.setOnClickListener {
                favInterface.onUserClick(model)
            }
            vbind.username.text = model.username

            loadGlide(model.photo, vbind.pict)
            val isAlreadyLiked = db.checkIfAlreadyFavorited(model.url)
            vbind.like.isLiked = isAlreadyLiked

            vbind.like.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    // This Method is not being used in this adapter
                }

                override fun unLiked(likeButton: LikeButton?) {
                    db.deleteByUsername(model.username)
                    mContext.makeLongToast("${model.username} ${mContext.getString(R.string.success_remove)}")
                    data.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyDataSetChanged()
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

    interface FavoriteAdapterInterface {
        fun onUserClick(user:FavoriteModel)
    }
}