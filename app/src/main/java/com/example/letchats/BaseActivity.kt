package com.example.letchats

import android.app.Activity
import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.toolbar_generic.*

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName

    var currentFragmentManager: FragmentManager? = null
        private set

   

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentFragmentManager = this.supportFragmentManager
        
    }
    
    /**
     * Remove all child fragment from  Parent Fragment in hosted Activity
     */
    fun popFragmentFromParentFragmentStack() {
        if (currentFragmentManager != null && currentFragmentManager!!.backStackEntryCount > 0) for (i in 0 until currentFragmentManager!!.backStackEntryCount) {
            currentFragmentManager!!.popBackStack()
        }
    }

    fun popFragmentFromStack() {
        if (currentFragmentManager!!.backStackEntryCount > 0) {
            Log.d(
                "IMEEXCEPTION",
                "Poped sucessfully with count " + currentFragmentManager!!.backStackEntryCount
            )
            hideKeyboard()
            val result = currentFragmentManager!!.popBackStackImmediate()
        }
    }

    val backStackSize: Int
        get() = currentFragmentManager!!.backStackEntryCount

    fun showTitleInToolbar(requesterFragmentTitle: String?) {
        Log.d(TAG, "showTitleInToolbar--- $requesterFragmentTitle")
        try {
            if (supportActionBar != null) {
                //supportActionBar?.title = requesterFragmentTitle
                if (requesterFragmentTitle != null && requesterFragmentTitle.isNotEmpty())
                    toolbarTitle.text = requesterFragmentTitle
                Log.d(TAG, "showTitleInToolbar: $requesterFragmentTitle")
            }
        } catch (e: Exception) {
        }
    }

    fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token
        // from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun replaceFragment(
        containerViewId: Int, fragment: Fragment?,
        setTransitionAnimation: Boolean
    ) {
        hideKeyboard()
        val fragmentTransaction = currentFragmentManager!!.beginTransaction()
        if (setTransitionAnimation) fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_to_left,
            R.anim.enter_from_right, R.anim.exit_to_left
        )
        fragmentTransaction.replace(containerViewId, fragment!!)
        fragmentTransaction.commit()
    }


    fun addFragmentToStack(
        containerViewId: Int, fragment: Fragment, bundle: Bundle?, tag: String?,
        setTransitionAnimation: Boolean
    ) {
        hideKeyboard()
        showTitleInToolbar(tag)
        val fragmentTransaction = currentFragmentManager!!.beginTransaction()
        if (setTransitionAnimation) fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_to_left,
            R.anim.enter_from_right, R.anim.exit_to_left
        )
        fragment.arguments = bundle
        fragmentTransaction.add(containerViewId, fragment)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    fun addFragmentToStackWithAnimation(
        containerViewId: Int, fragment: Fragment, tag: String?,
        setTransitionAnimation: Boolean,
        isFragmentAdd: Boolean
    ) {
        hideKeyboard()
        showTitleInToolbar(tag)
        val fragmentTransaction = currentFragmentManager!!.beginTransaction()
        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.sharedElementReturnTransition = DetailsTransition()
        fragment.enterTransition = Slide(Gravity.BOTTOM)
        if (isFragmentAdd) {
            fragmentTransaction.add(containerViewId, fragment, fragment.javaClass.simpleName)
            fragmentTransaction.addToBackStack(tag)
        } else {
            fragmentTransaction.replace(containerViewId, fragment, fragment.javaClass.simpleName)
        }
        fragmentTransaction.commit()
    }

    private inner class DetailsTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
        }
    }






}