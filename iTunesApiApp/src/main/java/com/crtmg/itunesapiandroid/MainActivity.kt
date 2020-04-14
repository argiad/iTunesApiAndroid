package com.crtmg.itunesapiandroid

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.crtmg.itunesapi.iTunesApi
import com.crtmg.itunesapi.iTunesSearchRequest
import kotlinx.android.synthetic.main.main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment()).commit()



        searchView.setOnCloseListener {
            Log.e("SEARCH", "ON CLOSE")
            return@setOnCloseListener true
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    if (it.length >= 3) iTunesApi.search(iTunesSearchRequest(it, limit = 30))
                }
                return true
            }

        })
    }


}
