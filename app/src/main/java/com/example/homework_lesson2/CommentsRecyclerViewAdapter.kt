package com.example.homework_lesson2

import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.homework_lesson2.placeholder.PlaceholderContent.PlaceholderItem
import com.example.homework_lesson2.databinding.FragmentCommentBinding

class CommentsRecyclerViewAdapter(private val values: List<PlaceholderItem>): RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.authorView.text = item.author.name
        holder.authorView.setTextColor(item.author.color)
        holder.contentView.text = item.comment
        holder.commentDateTextView.text = DateFormat.format("HH:mm dd.MM.yyyy", item.date)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        val authorView: TextView = binding.authorNameTextView
        val contentView: TextView = binding.commentTextView
        val commentDateTextView: TextView = binding.commentDateTextView
        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}