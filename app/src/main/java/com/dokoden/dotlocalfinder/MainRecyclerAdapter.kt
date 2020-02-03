package com.dokoden.dotlocalfinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dokoden.dotlocalfinder.databinding.MainRecyclerItemBinding

class MainRecyclerAdapter : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {
    var dataList: List<MainDataClass> = emptyList() //nullではなくemptyListを使うのがポイント

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            itemData = dataList[position]
            executePendingBindings()
        }
    }

    class ViewHolder(val binding: MainRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root)
}