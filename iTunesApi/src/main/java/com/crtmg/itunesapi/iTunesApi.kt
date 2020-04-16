package com.crtmg.itunesapi

import android.content.Context
import android.os.StrictMode


object iTunesApi {

    var callback: iTunesApiCallback? = null

    private var _context: Context? = null

    fun initWith(context: Context): iTunesApi {
        _context = context

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        return this
    }

    fun search(request: iTunesSearchRequest) {
        _context?.let {
            iTunesRequestManager.search(request, it)
        } ?: run {
            callback?.error(-1, "No context found")
        }
    }

}

interface iTunesApiCallback {
    fun error(id: Int, description: String)
    fun response(result: iTunesResult)
}
