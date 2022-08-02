package com.example.homework_lesson2.placeholder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

object PlaceholderContent {

    val ITEMS: MutableList<PlaceholderItem> = ArrayList()

    private lateinit var author: CommentAuthor

    fun addAuthor(author: CommentAuthor){
        this.author = author
    }

    fun addItem(comment: String) {
        ITEMS.add(PlaceholderItem(author, comment))
    }

    @Parcelize
    data class PlaceholderItem(val author: CommentAuthor, val comment: String, val date: Date = Date()) : Parcelable {
        override fun toString(): String = "$$date {author.name}: $comment"
    }

    @Parcelize
    data class CommentAuthor(val name: String, val color: Int) : Parcelable
}