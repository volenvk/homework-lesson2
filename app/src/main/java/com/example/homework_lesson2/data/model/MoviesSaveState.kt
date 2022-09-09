package com.example.homework_lesson2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MoviesSaveState(val movies: MutableMap<Int, MovieInfo> = mutableMapOf()): Parcelable

@Parcelize
data class MovieInfo(
    val image_id: Int? = null,
    val description: String? = null,
    var is_like: Boolean = false,
    var commentaries: PersonComments? = null
): Parcelable

@Parcelize
data class PersonComments(val commentaries: List<CommentItem> = listOf()): Parcelable

@Parcelize
data class CommentItem(val author: CommentAuthor, val comment: String, val date: Date = Date()): Parcelable

@Parcelize
data class CommentAuthor(val name: String, val color: Int): Parcelable