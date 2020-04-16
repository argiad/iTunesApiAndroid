package com.crtmg.itunesapiandroid.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<T>: Fragment() {

    private lateinit var mFragmentRootView: View

    var mListener: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.retainInstance = false
        mFragmentRootView = inflater.inflate(layoutId(), container, false)

        initView(mFragmentRootView)

        return mFragmentRootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            @Suppress("UNCHECKED_CAST")
            mListener = context as T
        } catch (e: ClassCastException) {
            throw ClassCastException(" $context must implement its hosted fragment listener")
        }

    }

    @LayoutRes abstract fun layoutId() :  Int
    abstract fun initView(fragmentRootView: View)
}