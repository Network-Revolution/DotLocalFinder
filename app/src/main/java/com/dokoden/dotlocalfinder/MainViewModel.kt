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

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.posick.mdns.Lookup
import org.xbill.DNS.AAAARecord
import org.xbill.DNS.ARecord
import org.xbill.DNS.DClass
import org.xbill.DNS.Type

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val liveDataList = MutableLiveData<List<MainDataClass>>()
    val localName = ObservableField("")
    val ipVersionList = listOf("[IPv6]", "IPv6", "IPv4")
    var selectedPosition = 0

    fun onResolve() {
        val resolveIpVersion = ipVersionList[selectedPosition]
        val mutableList = mutableListOf<MainDataClass>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (resolveIpVersion) {
                    "[IPv6]", "IPv6" -> {
                        wifiManager?.createMulticastLock("mDnsLock4IPv6")?.also {
                            it.setReferenceCounted(true)
                            it.acquire()
                            for (record in Lookup(localName.get(), Type.AAAA, DClass.IN).lookupRecords()) {
                                if (record.type == Type.AAAA) {
                                    mutableList += MainDataClass(
                                        localName.get()!!,
                                        resolveIpVersion,
                                        (record as AAAARecord).address.hostAddress
                                    )
                                }
                            }
                            it.release()
                        }
                    }
                    "IPv4" -> {
                        wifiManager?.createMulticastLock("mDnsLock4IPv4")?.also {
                            it.setReferenceCounted(true)
                            it.acquire()
                            for (record in Lookup(localName.get(), Type.A, DClass.IN).lookupRecords()) {
                                if (record.type == Type.A) {
                                    mutableList += MainDataClass(
                                        localName.get()!!,
                                        resolveIpVersion,
                                        (record as ARecord).address.hostAddress
                                    )
                                }
                            }
                            it.release()
                        }
                    }
                }
                liveDataList.postValue(mutableList.distinct())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}