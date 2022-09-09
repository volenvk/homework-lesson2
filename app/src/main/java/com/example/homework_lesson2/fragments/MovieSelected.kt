package com.example.homework_lesson2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homework_lesson2.data.INavigator.Companion.navigator
import com.example.homework_lesson2.data.model.MovieInfo
import com.example.homework_lesson2.data.model.PersonComments
import com.example.homework_lesson2.databinding.FragmentMovieSelectedBinding

class MovieSelected : Fragment() {

    private lateinit var binding: FragmentMovieSelectedBinding

    private lateinit var movieInfo: MovieInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieInfo = savedInstanceState?.getParcelable<MovieInfo>(KEY_MOVIE_INFO) ?:
                arguments?.getParcelable(ARG_MOVIE_INFO) ?:
                throw IllegalArgumentException("You need to specify info data to launch this fragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieSelectedBinding.inflate(inflater, container, false)
        binding.addCommentButton.setOnClickListener { navigator()?.showComments(movieInfo.commentaries) }
        binding.cinemaImageView.setImageResource(movieInfo.image_id!!)
        binding.cinemaAboutTextView.setText(movieInfo.description)
        binding.cinemaLikeCheckBox.isChecked = movieInfo.is_like
        binding.cinemaLikeCheckBox.setOnClickListener { movieInfo.is_like = binding.cinemaLikeCheckBox.isChecked }
        navigator()?.listenResult(PersonComments::class.java, viewLifecycleOwner) { movieInfo.commentaries = it }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_MOVIE_INFO, movieInfo)
    }

    override fun onDetach() {
        navigator()?.publishResult(movieInfo)
        super.onDetach()
    }

    companion object {
        @JvmStatic private val ARG_MOVIE_INFO = "arg_movie_info"
        @JvmStatic private val KEY_MOVIE_INFO = "key_movie_info"

        @JvmStatic
        fun newInstance(info: MovieInfo): MovieSelected{
            val args = Bundle()
            args.putParcelable(ARG_MOVIE_INFO, info)
            return MovieSelected().also { it.arguments = args }
        }
    }
}