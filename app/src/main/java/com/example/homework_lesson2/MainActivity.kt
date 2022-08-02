package com.example.homework_lesson2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.homework_lesson2.data.model.MovieInfoData
import com.example.homework_lesson2.data.model.Movies
import com.example.homework_lesson2.data.model.Options
import com.example.homework_lesson2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        createCinemaList(binding, Options.DEFAULT.count)
    }

    override fun onResume() {
        super.onResume()
        setCinemaTitleColor()
    }

    private fun setCinemaTitleColor() {
        if (index >= 0) {
            Log.i("setCinemaTitleColor", "cinemaIndex: ${index}")
            val id = binding.flowCinema.referencedIds[index]
            val cinemaBinding = binding.root.getViewById(id)?.tag as? ItemCinemaBinding
            cinemaBinding?.let {
                Log.d("cinemaTitleTextView", "SetTextColor: gray")
                it.cinemaTitleTextView.setTextColor(Color.GRAY)
            }
        }
    }

    private fun createCinemaList(binding: ActivityMainBinding, count: Int){
        val mapIterator = Movies.movies_posters.iterator()
        val cinemaBindings = (0 until count).map { index ->
            val cinemaBinding = ItemCinemaBinding.inflate(layoutInflater)
            cinemaBinding.root.id = View.generateViewId()
            if (mapIterator.hasNext()){
                val next = mapIterator.next()
                cinemaBinding.cinemaImageView.setImageResource(next.second)
                cinemaBinding.cinemaTitleTextView.text = next.first
                cinemaBinding.root.tag = cinemaBinding
                cinemaBinding.cinemaDetailsButton.setOnClickListener { showItemSelected(index, next) }
                binding.root.addView(cinemaBinding.root)
            } else cinemaBinding.root.tag = null
            cinemaBinding
        }
        binding.flowCinema.referencedIds = cinemaBindings.filter { it.root.tag != null }.map { it.root.id }.toIntArray()
        Log.d("createCinemaList", "referencedIds: ${binding.flowCinema.referencedIds.size}")
    }

    private fun showItemSelected(index:Int, pair: Pair<String, Int>){
        if (index >= 0) {
            saveData.index = index
            resultCinemaSelectedLauncher.launch(
                CinemaSelectionRequest(
                    cinema_info =  MovieInfoData(pair.second, Movies.movies_info[pair.first]),
                    save_state = saveData.states[index]
                ))
        }
    }
}