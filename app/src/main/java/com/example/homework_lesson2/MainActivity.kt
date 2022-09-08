package com.example.homework_lesson2

import android.os.Bundle
import com.example.homework_lesson2.data.BaseActivity
import com.example.homework_lesson2.data.INavigator
import com.example.homework_lesson2.data.model.MovieInfo
import com.example.homework_lesson2.data.model.Options
import com.example.homework_lesson2.databinding.ActivityMainBinding
import com.example.homework_lesson2.fragments.Comments
import com.example.homework_lesson2.fragments.ListMovies
import com.example.homework_lesson2.fragments.MovieSelected

class MainActivity : BaseActivity(), INavigator {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        ListMovies.newInstance(Options.DEFAULT).launch()
    }

    override fun showMovieSelected(info: MovieInfo) {
        MovieSelected.newInstance(info).launch()
    }

    override fun showComments() {
        Comments.newInstance().launch()
    }

    override fun goBack() {
        onBackPressed()
    }
}