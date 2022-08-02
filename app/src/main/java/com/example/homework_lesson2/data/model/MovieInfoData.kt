package com.example.homework_lesson2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieInfoData(val image_id: Int? = null, val description: String? = null): Parcelable