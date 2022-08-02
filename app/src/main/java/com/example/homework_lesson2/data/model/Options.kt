package com.example.homework_lesson2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(val count: Int): Parcelable {
    companion object {
        @JvmStatic val DEFAULT = Options(count = 5)
    }
}
