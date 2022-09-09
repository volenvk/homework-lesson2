package com.example.homework_lesson2.data

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.homework_lesson2.data.model.MovieInfo
import com.example.homework_lesson2.data.model.PersonComments

interface INavigator {
    fun showMovieSelected(info: MovieInfo)
    fun showComments(commentaries: PersonComments?)
    fun goBack()
    fun <T: Parcelable> publishResult(result: T)
    fun <T: Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: (T) -> Unit)

    companion object {
        fun Fragment.navigator(): INavigator? = requireActivity() as? INavigator
    }
}