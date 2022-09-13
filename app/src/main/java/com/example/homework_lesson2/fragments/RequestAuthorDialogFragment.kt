package com.example.homework_lesson2.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.homework_lesson2.R
import com.example.homework_lesson2.data.INavigator.Companion.navigator
import com.example.homework_lesson2.data.model.CommentAuthor
import com.example.homework_lesson2.databinding.LayoutInputNameAuthorBinding
import java.util.*
import kotlin.properties.Delegates

class RequestAuthorDialogFragment: DialogFragment() {

    private lateinit var dialogBinding: LayoutInputNameAuthorBinding
    private var showAlert by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAlert = savedInstanceState?.getBoolean(KEY_ALERT_REQUEST) ?: arguments?.getBoolean(ARG_ALERT_REQUEST) ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = LayoutInputNameAuthorBinding.inflate(layoutInflater)
        if (showAlert) {
            dialogBinding.authorInputEditText.error = ALERT_AUTHOR_NAME
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.default_alert_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.action_yes) { _, _ ->
                val name: String = dialogBinding.authorInputEditText.text.toString()
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_AUTHOR_RESPONSE to name))
            }
            .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_ALERT_REQUEST, showAlert)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "Dialog dismissed")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        navigator()?.goBack()
    }

    companion object {
        @JvmStatic val ALERT_AUTHOR_NAME = "Пожалуйста введите свое имя"

        @JvmStatic val TAG = RequestAuthorDialogFragment::class.java.simpleName
        @JvmStatic private val KEY_AUTHOR_RESPONSE = "key_author_response"
        @JvmStatic private val KEY_ALERT_REQUEST = "key_alert_request"
        @JvmStatic private val ARG_ALERT_REQUEST = "arg_alert_request"
        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        @JvmStatic
        fun newInstance(showAlert: Boolean = false): RequestAuthorDialogFragment{
            val args = Bundle()
            args.putBoolean(ARG_ALERT_REQUEST, showAlert)
            return RequestAuthorDialogFragment().also { it.arguments = args }
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (CommentAuthor) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(CommentAuthor(result.getString(KEY_AUTHOR_RESPONSE)!!, makeRandomColor()))
            })
        }

        private fun makeRandomColor(): Int{
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
    }
}