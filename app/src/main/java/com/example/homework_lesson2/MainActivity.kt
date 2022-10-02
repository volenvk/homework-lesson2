package com.example.homework_lesson2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.homework_lesson2.data.BaseActivity
import com.example.homework_lesson2.data.INavigator
import com.example.homework_lesson2.data.model.CommentAuthor
import com.example.homework_lesson2.data.model.MovieInfo
import com.example.homework_lesson2.data.model.Options
import com.example.homework_lesson2.data.model.PersonComments
import com.example.homework_lesson2.databinding.ActivityMainBinding
import com.example.homework_lesson2.fragments.Comments
import com.example.homework_lesson2.fragments.ListMovies
import com.example.homework_lesson2.fragments.MovieSelected
import com.example.homework_lesson2.fragments.RequestAuthorDialogFragment


class MainActivity : BaseActivity(), INavigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState == null) ListMovies.newInstance(Options.DEFAULT).launch()
    }

    override fun showMovieSelected(info: MovieInfo) {
        MovieSelected.newInstance(info).launch()
    }

    override fun showComments(commentaries: PersonComments?) {
        Comments.newInstance(commentaries).launch()
    }

    override fun showAuthorDialog() {
        val dialog = RequestAuthorDialogFragment.newInstance()
        dialog.show(supportFragmentManager, RequestAuthorDialogFragment.TAG)
    }

    override fun showAuthorAlertDialog() {
        val dialog = RequestAuthorDialogFragment.newInstance(true)
        dialog.show(supportFragmentManager, RequestAuthorDialogFragment.TAG)
    }

    override fun setupResultDialog(result: (CommentAuthor) -> Unit) {
        RequestAuthorDialogFragment.setupListener(supportFragmentManager, this) { result(it) }
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun showToast(message: String, duration: Int){
        Toast.makeText(this, message, duration).show()
    }

    override fun sharedApp() {
        val sendIntent = Intent().also {
            it.action = Intent.ACTION_SEND
            it.putExtra(Intent.EXTRA_SUBJECT, "App")
            it.putExtra(Intent.EXTRA_TEXT, R.string.shared_app)
            it.type = "text/plain"
        }
        startActivity(sendIntent)
    }
}