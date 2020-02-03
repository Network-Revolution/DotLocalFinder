package com.dokoden.dotlocalfinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dokoden.dotlocalfinder.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        val recyclerAdapter = MainRecyclerAdapter()

        binding.viewModel = mainViewModel
        binding.recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
        }

        mainViewModel.liveDataList.observe(this, Observer {
            it?.let {
                recyclerAdapter.dataList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        })
        return binding.root
    }
}
