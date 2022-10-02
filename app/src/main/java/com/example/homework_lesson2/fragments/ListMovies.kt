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


class ListMovies : Fragment() {

    private lateinit var binding: FragmentListMoviesBinding
    private lateinit var options: Options
    private var moviesSaveState: MoviesSaveState? = null
    private var movieSelected: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable<Options>(KEY_OPTIONS) ?:
                arguments?.getParcelable(ARG_OPTIONS) ?:
                throw IllegalArgumentException("You need to specify options to launch this fragment")
        moviesSaveState = savedInstanceState?.getParcelable<MoviesSaveState>(KEY_CINEMA_STATES) ?:
                arguments?.getParcelable(ARG_CINEMA_STATES)?:
                throw IllegalArgumentException("You need to specify options to launch this fragment")
        savedInstanceState?.getInt(KEY_CINEMA_INDEX_SELECTED, -1)?.takeIf { it > -1 }?.let { movieSelected = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListMoviesBinding.inflate(inflater, container, false)
        createCinemaList(options.count)
        navigator()?.listenResult(MovieInfo::class.java, viewLifecycleOwner) { info ->
            movieSelected?.let { moviesSaveState!!.movies[it] = info }
        }
        movieSelected?.let { setCinemaTitleColor(it) }
        binding.sharedButton.setOnClickListener { navigator()?.sharedApp() }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
        moviesSaveState?.let { outState.putParcelable(KEY_CINEMA_STATES, it) }
        movieSelected?.let { outState.putInt(KEY_CINEMA_INDEX_SELECTED, it) }
    }

    private fun setCinemaTitleColor(index: Int) {
        Log.d("setCinemaTitleColor", "cinemaIndex: $index")
        val id = binding.flowCinema.referencedIds[index]
        val cinemaBinding = binding.root.getViewById(id)?.tag as? MovieItemBinding
        cinemaBinding?.let {
            Log.d("cinemaTitleTextView", "SetTextColor: gray")
            it.cinemaTitleTextView.setTextColor(Color.GRAY)
        }
    }

    private fun createCinemaList(count: Int){
        val mapIterator = Movies.movies_posters.iterator()
        val movieBindings = (0 until count).map { index ->
            val movieBinding = MovieItemBinding.inflate(layoutInflater)
            movieBinding.root.id = View.generateViewId()
            if (mapIterator.hasNext()){
                val next = mapIterator.next()
                val info = MovieInfo(image_id = next.second, description = Movies.movies_info[next.first])
                movieBinding.cinemaImageView.setImageResource(next.second)
                movieBinding.cinemaTitleTextView.text = next.first
                movieBinding.root.tag = movieBinding
                movieBinding.cinemaDetailsButton.setOnClickListener {
                    movieSelected = index
                    navigator()?.showMovieSelected(moviesSaveState!!.movies[index] ?: info)
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
        @JvmStatic private val ARG_CINEMA_STATES = "arg_cinema_states"
        @JvmStatic private val KEY_OPTIONS = "key_options"
        @JvmStatic private val KEY_CINEMA_STATES = "key_cinema_states"
        @JvmStatic private val KEY_CINEMA_INDEX_SELECTED = "key_cinema_index_selected"

        @JvmStatic
        fun newInstance(options: Options): ListMovies{
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            args.putParcelable(ARG_CINEMA_STATES, MoviesSaveState())
            return ListMovies().also { it.arguments = args }
        }
    }
}