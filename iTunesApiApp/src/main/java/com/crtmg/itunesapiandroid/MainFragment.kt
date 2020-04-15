package com.crtmg.itunesapiandroid

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        fragmentRootView.rvList.addItemDecoration(GridSpacingItemDecoration(30))
        fragmentRootView.rvList.adapter = adapter

    }


}