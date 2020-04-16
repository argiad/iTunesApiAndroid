package com.crtmg.itunesapiandroid.view.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crtmg.itunesapiandroid.view.GridSpacingItemDecoration
import com.crtmg.itunesapiandroid.R
import com.crtmg.itunesapiandroid.view.adapter.ResultListAdapter
import com.crtmg.itunesapiandroid.data.RoomHelper
import com.crtmg.itunesapiandroid.view.MainActivityInterface
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : BaseFragment<MainActivityInterface>() {

    private lateinit var adapter: ResultListAdapter

    fun showFav() {
        adapter.apply {
            itemList = RoomHelper.getFavList() ?: arrayListOf()
            notifyDataSetChanged()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(fragmentRootView: View) {
        adapter = ResultListAdapter()

        fragmentRootView.rvList.layoutManager =
            GridLayoutManager(this.context, 2) as RecyclerView.LayoutManager?
        fragmentRootView.rvList.addItemDecoration(
            GridSpacingItemDecoration(
                30
            )
        )
        fragmentRootView.rvList.adapter = adapter

    }


}