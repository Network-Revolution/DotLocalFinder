/*
 *    Copyright 2020- Network Revolution Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.dokoden.dotlocalfinder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dokoden.dotlocalfinder.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import org.xbill.DNS.Type

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val mainViewModel by viewModels<MainViewModel>()
        val recyclerAdapter = MainRecyclerAdapter(object : MainRecyclerAdapter.OnCardClickListener {
            override fun onCardClicked(mainDataClass: MainDataClass) {
                val copyAddress = when (mainDataClass.resolveType) {
                    Type.AAAA -> "[${mainDataClass.ipAddress}]"
                    else -> mainDataClass.ipAddress
                }
                val action = mainViewModel.actionList[mainViewModel.selectedPosition]
                when (action) {
                    "Clipboard" -> {
                        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("mDNS", copyAddress))
                        // clipboard.primaryClip = ClipData.newPlainText("mDNS", mainDataClass.ipAddress))
                        Snackbar.make(view!!, "Copied the IP address : $copyAddress", Snackbar.LENGTH_SHORT).show()
                    }
                    "HTTP" -> {
                        val uri = Uri.parse("http://${copyAddress}/")
                        Intent(Intent.ACTION_VIEW, uri).also {
                            startActivity(it)
                        }
                    }
                    "HTTPS" -> {
                        val uri = Uri.parse("https://${copyAddress}/")
                        Intent(Intent.ACTION_VIEW, uri).also {
                            startActivity(it)
                        }
                    }
                }
            }
        })
        mainViewModel.liveDataList.observe(viewLifecycleOwner) {
            it?.also {
                recyclerAdapter.dataList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        }

        MainFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = mainViewModel
            it.itemAdapter = recyclerAdapter
            return it.root
        }
    }
}
