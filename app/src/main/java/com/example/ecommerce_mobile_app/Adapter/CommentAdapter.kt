package com.example.ecommerce_mobile_app.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Model.Comments
import com.example.ecommerce_mobile_app.R

class CommentAdapter(private val commentsList: List<Comments>,
                     private val onEditClick: (Comments) -> Unit) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        val editCommentIcon: ImageView = itemView.findViewById(R.id.editCommentIcon)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.commentTextView.text = comment.comment
        holder.editCommentIcon.setOnClickListener {
            onEditClick(comment)
        }
    }

    override fun getItemCount(): Int = commentsList.size


}