package com.aqsa.consumer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.RecyclerView
import com.aqsa.consumer.databinding.ItemUserBinding
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UserFavoriteAdapter() :
    RecyclerView.Adapter<UserFavoriteAdapter.FollowViewHolder>() {
    lateinit var favInterface: FavoriteAdapterInterface
    private val data = mutableListOf<FavoriteModel>()

    fun setData(newData: MutableList<FavoriteModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
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
            vbind.username.text = model.username
            loadGlide(model.photo, vbind.pict)

            vbind.btnShare.setOnClickListener {
                favInterface.onShareClick(model)
            }
        }


        private fun loadGlide(avatarUrl: String, pict: CircleImageView) {
            Glide
                .with(vbind.root)
                .load(avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.loading_plc)
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
        fun onShareClick(user: FavoriteModel)
    }
}