package com.amishgarg.wartube.Adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amishgarg.wartube.PicassoUtil
import com.amishgarg.wartube.Model.Comment
import com.amishgarg.wartube.R
import com.amishgarg.wartube.TimeDisplay
import kotlinx.android.synthetic.main.comment_item.view.*


class CommentListAdapter : ListAdapter<Comment, CommentListAdapter.ItemViewholder>(DiffCallback2()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.comment_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Comment) = with(itemView) {
            this.post_author_namec.text = item.author.display_name
            PicassoUtil.loadImagePicasso(item.author.profile_pic, this.post_author_iconc)
            val xyz : String = item.timestamp as String
            this.post_timestampc.text = TimeDisplay(xyz.toLong()).getTimeDisplay()
            this.post_textc.text = item.commentText


            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}

class DiffCallback2 : DiffUtil.ItemCallback<Comment>() {


    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem== newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}
