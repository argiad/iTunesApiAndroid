package com.crtmg.itunesapiandroid

import android.app.Application
import com.crtmg.itunesapi.iTunesApi
import com.crtmg.itunesapiandroid.data.RoomHelper

class iTunesApiApp: Application() {

    override fun onCreate() {
        super.onCreate()

        iTunesApi.initWith(applicationContext)
        RoomHelper.initDB(applicationContext)
    }
}