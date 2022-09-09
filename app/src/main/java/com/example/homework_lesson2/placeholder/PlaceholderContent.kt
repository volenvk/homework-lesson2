package com.example.homework_lesson2.placeholder

import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.homework_lesson2.data.model.CommentAuthor
import com.example.homework_lesson2.data.model.CommentItem
import com.example.homework_lesson2.data.model.PersonComments
import kotlinx.parcelize.Parcelize
import java.util.*

class PlaceholderContent(owner: LifecycleOwner) {

    private val liveData : MutableLiveData<PlaceholderItem> = MutableLiveData()
    val values: MutableList<PlaceholderItem> = ArrayList()

    init {
        liveData.observe(owner, Observer<PlaceholderItem> { values.add(it) })
    }

    fun addItem(author: CommentAuthor, comment: String) {
        liveData.postValue(PlaceholderItem(author, comment))
    }

    fun addRange(comments: List<CommentItem>){
        comments.forEach{ item ->
            values.add(PlaceholderItem(author = item.author, comment = item.comment, date = item.date))
        }
    }

    fun getCommentaries(): PersonComments {
        return PersonComments(values.map { CommentItem(it.author, it.comment, it.date) })
    }

    @Parcelize
    data class PlaceholderItem(val author: CommentAuthor, val comment: String, val date: Date = Date()) : Parcelable {
        override fun toString(): String = "$date ${author.name}: $comment"
    }
}