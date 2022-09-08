package com.example.homework_lesson2.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.homework_lesson2.data.INavigator.Companion.navigator
import com.example.homework_lesson2.data.model.*
import com.example.homework_lesson2.databinding.FragmentListMoviesBinding
import com.example.homework_lesson2.databinding.MovieItemBinding
import kotlin.properties.Delegates

class ListMovies : Fragment() {

    private lateinit var binding: FragmentListMoviesBinding
    private lateinit var options: Options
    private var moviesSaveState: MoviesSaveState = MoviesSaveState()
    private var movieSelected: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable<Options>(KEY_OPTIONS) ?:
                arguments?.getParcelable(ARG_OPTIONS) ?:
                throw IllegalArgumentException("You need to specify options to launch this fragment")

        savedInstanceState?.getParcelable<MoviesSaveState>(KEY_CINEMA_STATES)?.let { moviesSaveState = it }
        savedInstanceState?.getInt(KEY_CINEMA_SELECTED_INDEX)?.let { movieSelected = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)
        navigator()?.listenResult(MovieInfo::class.java, viewLifecycleOwner) { info ->
            movieSelected?.let {
                setCinemaTitleColor(it)
                moviesSaveState.movies[it] = info
            }
        }
        createCinemaList(options.count, savedInstanceState)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
        outState.putParcelable(KEY_CINEMA_STATES, moviesSaveState)
        movieSelected?.let { outState.putInt(KEY_CINEMA_SELECTED_INDEX, it) }
    }

    private fun setCinemaTitleColor(index: Int) {
        Log.i("setCinemaTitleColor", "cinemaIndex: $index")
        val id = binding.flowCinema.referencedIds[index]
        val cinemaBinding = binding.root.getViewById(id)?.tag as? MovieItemBinding
        cinemaBinding?.let {
            Log.d("cinemaTitleTextView", "SetTextColor: gray")
            it.cinemaTitleTextView.setTextColor(Color.GRAY)
        }
    }

    private fun createCinemaList(count: Int, outState: Bundle?){
        val mapIterator = Movies.movies_posters.iterator()
        val movieBindings = (0 until count).map { index ->
            val movieBinding = MovieItemBinding.inflate(layoutInflater)
            movieBinding.root.id = View.generateViewId()
            if (mapIterator.hasNext()){
                val next = mapIterator.next()
                val info = MovieInfo(image_id = next.second, description = Movies.movies_info[next.first])
                moviesSaveState.movies[index] = info
                movieBinding.cinemaImageView.setImageResource(next.second)
                movieBinding.cinemaTitleTextView.text = next.first
                movieBinding.root.tag = movieBinding
                movieBinding.cinemaDetailsButton.setOnClickListener {
                    movieSelected = index
                    navigator()?.showMovieSelected(info)
                }
                binding.root.addView(movieBinding.root)
            } else movieBinding.root.tag = null
            movieBinding
        }
        binding.flowCinema.referencedIds = movieBindings.filter { it.root.tag != null }.map { it.root.id }.toIntArray()
        Log.d("createCinemaList", "referencedIds: ${binding.flowCinema.referencedIds.size}")
    }

    companion object {
        @JvmStatic private val ARG_OPTIONS = "arg_options_list_movies"
        @JvmStatic private val KEY_OPTIONS = "KEY_OPTIONS"
        @JvmStatic private val KEY_CINEMA_STATES = "key_cinema_states"
        @JvmStatic private val KEY_CINEMA_SELECTED_INDEX = "key_cinema_selected_index"

        @JvmStatic
        fun newInstance(options: Options): ListMovies{
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            return ListMovies().also { it.arguments = args }
        }
    }
}