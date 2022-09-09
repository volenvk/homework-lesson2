package com.example.homework_lesson2.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.homework_lesson2.CommentsRecyclerViewAdapter
import com.example.homework_lesson2.data.INavigator.Companion.navigator
import com.example.homework_lesson2.data.model.CommentAuthor
import com.example.homework_lesson2.data.model.PersonComments
import com.example.homework_lesson2.databinding.FragmentCommentsListBinding
import com.example.homework_lesson2.placeholder.PlaceholderContent
import kotlin.properties.Delegates

class Comments : Fragment() {

    private lateinit var binding: FragmentCommentsListBinding
    private val placeholderContent: PlaceholderContent = PlaceholderContent(this)

    private var commentaries: PersonComments? = null
    private var author: CommentAuthor by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentaries = savedInstanceState?.getParcelable<PersonComments>(KEY_COMMENTARIES) ?: arguments?.getParcelable(ARG_COMMENTARIES)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentsListBinding.inflate(inflater, container, false)
        binding.list.layoutManager = GridLayoutManager(context, GRID_LAYOUT_COLUMN_COUNT)
        commentaries?.let { placeholderContent.addRange(it.commentaries) }
        binding.list.adapter = CommentsRecyclerViewAdapter(placeholderContent.values)
        setOnCommentEditListener()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_COMMENTARIES, placeholderContent.getCommentaries())
    }

    override fun onDetach() {
        navigator()?.publishResult(placeholderContent.getCommentaries())
        super.onDetach()
    }

    private fun setOnCommentEditListener() {
        binding.cinemaCommentTextEdit.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val editText = view as TextView
                placeholderContent.addItem(author, editText.text.toString())
                editText.text = ""
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    companion object {
        @JvmStatic val GRID_LAYOUT_COLUMN_COUNT = 1
        @JvmStatic val ARG_COMMENTARIES = "arg_commentaries"
        @JvmStatic val KEY_COMMENTARIES = "key_commentaries";

        @JvmStatic
        fun newInstance(commentaries: PersonComments?): Comments{
            val args = Bundle()
            args.putParcelable(ARG_COMMENTARIES, commentaries)
            return Comments().also { it.arguments = args }
        }
    }
}