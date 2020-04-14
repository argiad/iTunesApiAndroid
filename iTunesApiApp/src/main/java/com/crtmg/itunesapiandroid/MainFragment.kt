package com.crtmg.itunesapiandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private lateinit var adapter: ResultListAdapter

    private lateinit var mFragmentRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.retainInstance = false
        mFragmentRootView = inflater.inflate(R.layout.fragment_main, container, false)

        initView(mFragmentRootView)

        return mFragmentRootView
    }

    private fun initView(fragmentRootView: View) {
        adapter = ResultListAdapter()

        fragmentRootView.rvList.layoutManager = GridLayoutManager(this.context, 2) as RecyclerView.LayoutManager?
        fragmentRootView.rvList.addItemDecoration(GridSpacingItemDecoration(30))
        fragmentRootView.rvList.adapter = adapter
    }


}