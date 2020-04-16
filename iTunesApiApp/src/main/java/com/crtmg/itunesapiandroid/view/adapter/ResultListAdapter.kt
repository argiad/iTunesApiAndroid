package com.crtmg.itunesapiandroid.view.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.collection.LruCache
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.crtmg.itunesapi.iTunesApi
import com.crtmg.itunesapi.iTunesApiCallback
import com.crtmg.itunesapi.iTunesItem
import com.crtmg.itunesapi.iTunesResult
import com.crtmg.itunesapiandroid.R
import com.crtmg.itunesapiandroid.view.MainActivityInterface
import kotlinx.android.synthetic.main.album_item.view.*

class ResultListAdapter : RecyclerView.Adapter<ResultListAdapter.ResultViewHolder>() {

    var itemList: List<iTunesItem> = arrayListOf()

    init {

        iTunesApi.callback = object : iTunesApiCallback {
            override fun error(id: Int, description: String) {
                Log.e("BLApiCallback", " Error # $id Error description  $description")
            }

            override fun response(result: iTunesResult) {
                itemList = result.results
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.album_item, parent, false)
        return ResultViewHolder(view)

    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) =
        holder.bind(itemList[position])


    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: iTunesItem) {
            itemView.progres_item.visibility = View.VISIBLE
            itemView.author_name.text = item.artistName
            itemView.album_name_tv.text = item.trackName
            loadImage(item)

            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    (v.context as? MainActivityInterface)?.showDetailFor(item)
                    return@setOnTouchListener true
                }
                return@setOnTouchListener false
            }
        }

        private fun loadImage(item: iTunesItem) {

            val mRequestQueue = Volley.newRequestQueue(itemView.album_cover_image.context)
            val mImageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
                private val mCache: LruCache<String, Bitmap> =
                    LruCache<String, Bitmap>(200)

                override fun getBitmap(url: String): Bitmap? {
                    return mCache.get(url)
                }

                override fun putBitmap(url: String, bitmap: Bitmap) {
                    mCache.put(url, bitmap)
                    itemView.progres_item.visibility = View.GONE
                }
            })

            itemView.album_cover_image.setImageUrl(item.artworkUrl100, mImageLoader)
        }
    }
}