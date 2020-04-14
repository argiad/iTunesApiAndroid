package com.crtmg.itunesapiandroid

import android.app.Application
import com.crtmg.itunesapi.iTunesApi

class iTunesApiApp: Application() {

    override fun onCreate() {
        super.onCreate()

        iTunesApi.initWith(applicationContext)
    }
}