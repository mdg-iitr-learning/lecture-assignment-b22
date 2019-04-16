package com.amishgarg.wartube.Adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.amishgarg.wartube.PicassoUtil
import com.amishgarg.wartube.Model.Post
import com.amishgarg.wartube.R
import com.amishgarg.wartube.TimeDisplay
import kotlinx.android.synthetic.main.posts_item.view.*


class PostListAdapter : ListAdapter<Post, PostListAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.posts_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostListAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }


    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Post) = with(itemView) {
            // TODO: Bind the data with View
            this.post_author_name.text = item.author.display_name

            PicassoUtil.loadImagePicasso(item.full__url, this.post_photo)

            PicassoUtil.loadImagePicasso(item.author.profile_pic, this.post_author_icon)
            this.post_text.text = item.text
            this.post_timestamp.text = TimeDisplay(item.timestamp as Long).getTimeDisplay()
            this.post_num_likes.text = "${item.likes}"
            this.post_num_com.text = "${item.comments}"

            val postsKey = item.author.uid+item.timestamp

            setOnClickListener {

                val args : Bundle = Bundle()
                args.putString("postKey", postsKey)

                findNavController().navigate(R.id.post_detail_dest, args)
            }



        }


    }


}

class DiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return (oldItem.timestamp == newItem.timestamp && oldItem.author.uid == newItem.author.uid)
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
