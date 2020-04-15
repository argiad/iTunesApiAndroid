package com.crtmg.itunesapiandroid

import android.graphics.Bitmap
import android.view.View
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley
import com.crtmg.itunesapi.iTunesItem
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : BaseFragment<MainActivityInterface>() {

    var item: iTunesItem? = null

    override fun layoutId(): Int {
        return R.layout.fragment_detail
    }

    override fun initView(fragmentRootView: View) {
        item?.let {
            loadImage(it, fragmentRootView.imageView)
            fragmentRootView.tvArtistName.text = it.artistName
            fragmentRootView.tvTrackName.text = it.trackName
            fragmentRootView.swLike.isChecked = RoomHelper.isInFav(it)

            fragmentRootView.swLike.setOnCheckedChangeListener { _, isChecked ->
                RoomHelper.changeState(isChecked, it)
            }
        }

    }

    private fun loadImage(item: iTunesItem, imageView: NetworkImageView) {

        val mRequestQueue = Volley.newRequestQueue(context)
        val mImageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
            private val mCache: LruCache<String, Bitmap> =
                LruCache<String, Bitmap>(200)

            override fun getBitmap(url: String): Bitmap? {
                return mCache.get(url)
            }

            override fun putBitmap(url: String, bitmap: Bitmap) {
                mCache.put(url, bitmap)
//                item.progres_item.visibility = View.GONE
            }
        })

        imageView.setImageUrl(item.artworkUrl100, mImageLoader)
    }
}