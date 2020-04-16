package com.crtmg.itunesapi

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

internal object iTunesRequestManager {

    fun search(request: iTunesSearchRequest, context: Context) {

        val queue = Volley.newRequestQueue(context)

        val inlineParams = "?" + request.parameters

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,
            Constants.iTunesPath + Constants.iTunesPathSearchComponent + inlineParams,
            null,
            Response.Listener {
                val json = Json(JsonConfiguration(ignoreUnknownKeys = true))
                val obj = json.parse(iTunesResult.serializer(), it.toString())
                iTunesApi.callback?.response(obj)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                iTunesApi.callback?.error(-2, error.toString())
            })

        val policy: RetryPolicy = DefaultRetryPolicy(
            request.requestTimeOut,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonRequest.retryPolicy = policy
        queue.add(jsonRequest)

    }

}