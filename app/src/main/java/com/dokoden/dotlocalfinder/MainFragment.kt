package com.dokoden.dotlocalfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dokoden.dotlocalfinder.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainViewModel by viewModels<MainViewModel>()
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        val recyclerAdapter = MainRecyclerAdapter()

        binding.viewModel = mainViewModel
        binding.recyclerView.also {
            it.adapter = recyclerAdapter
            it.layoutManager = LinearLayoutManager(context)
        }

        mainViewModel.liveDataList.observe(viewLifecycleOwner, Observer {
            it?.also {
                recyclerAdapter.dataList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        })
        return binding.root
    }
}
