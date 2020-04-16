package com.crtmg.itunesapiandroid.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.crtmg.itunesapi.iTunesApi
import com.crtmg.itunesapi.iTunesItem
import com.crtmg.itunesapi.iTunesSearchRequest
import com.crtmg.itunesapiandroid.R
import com.crtmg.itunesapiandroid.view.fragment.DetailFragment
import com.crtmg.itunesapiandroid.view.fragment.MainFragment
import kotlinx.android.synthetic.main.main.*


class MainActivity : AppCompatActivity(),
    MainActivityInterface {

    val mainFragment = MainFragment()
    val detailFragment =
        DetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        supportFragmentManager.beginTransaction().replace(R.id.container, mainFragment).commit()

        searchView.setOnCloseListener {
            Log.e("SEARCH", "ON CLOSE")
            return@setOnCloseListener true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.length >= 3) iTunesApi.search(iTunesSearchRequest(it, limit = 30))
                }
                return true
            }

        })

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus && (supportFragmentManager.findFragmentById(R.id.container) is DetailFragment))
                supportFragmentManager.beginTransaction().replace(R.id.container, mainFragment)
                    .commit()

        }
    }

    override fun onResume() {
        super.onResume()

        if (!checkFocusRec(searchView)) {
            mainFragment.showFav()
        }
    }


    private fun checkFocusRec(view: View): Boolean {
        if (view.isFocused) return true
        if (view is ViewGroup) {
            val viewGroup = view as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                if (checkFocusRec(viewGroup.getChildAt(i))) return true
            }
        }
        return false
    }

    override fun showDetailFor(item: iTunesItem) {
        detailFragment.item = item
        supportFragmentManager.beginTransaction().replace(R.id.container, detailFragment).commit()
        searchView.clearFocus()

        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.container) is DetailFragment) {
            supportFragmentManager.beginTransaction().replace(R.id.container, mainFragment).commit()
        } else
            super.onBackPressed()
    }
}


interface MainActivityInterface {
    fun showDetailFor(item: iTunesItem)
}