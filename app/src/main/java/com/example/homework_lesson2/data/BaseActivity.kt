package com.example.homework_lesson2.data

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.homework_lesson2.R

abstract class BaseActivity: AppCompatActivity() {

    private val currentFragment: Fragment? get() = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer)

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    init {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    fun Fragment.launch(){
        if (currentFragment == null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainFragmentContainer, this)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.mainFragmentContainer, this)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: (T) -> Unit) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner, FragmentResultListener { _, bundle ->
            listener(bundle.getParcelable(KEY_RESULT)!!)
        })
    }

    open fun updateUI() { }

    companion object {
        @JvmStatic private val KEY_RESULT = "key_result"
    }
}