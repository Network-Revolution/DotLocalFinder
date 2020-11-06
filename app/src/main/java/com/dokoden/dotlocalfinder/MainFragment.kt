package com.dokoden.dotlocalfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dokoden.dotlocalfinder.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainViewModel by viewModels<MainViewModel>()
        val recyclerAdapter = MainRecyclerAdapter()

        mainViewModel.liveDataList.observe(viewLifecycleOwner, {
            it?.also {
                recyclerAdapter.dataList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        MainFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = mainViewModel
            it.itemAdapter = recyclerAdapter
            return it.root
        }
    }
}
