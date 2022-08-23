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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.posick.mdns.Lookup
import org.xbill.DNS.*
import java.util.concurrent.atomic.AtomicBoolean

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val wifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager?
    val liveDataList = MutableLiveData<List<MainDataClass>>()
    val localName = ObservableField("")
    val actionList = listOf("Clipboard", "HTTP", "HTTPS")
    var selectedPosition = 0

    fun onResolve() {
        val mutableList = mutableListOf<MainDataClass>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lookupRecords = arrayListOf<Record>()
                wifiManager?.createMulticastLock("mDnsLock")?.also {
                    it.setReferenceCounted(true)
                    it.acquire()
                    localName.get()?.also { name ->
                        val done = AtomicBoolean(false)
                        val haveV4 = AtomicBoolean(false)
                        val haveV6 = AtomicBoolean(false)
                        val resultHandlerSync = Object()

                        val resolver = object : Lookup.RecordListener {
                            override fun receiveRecord(id: Any?, record: Record?) {
                                synchronized(resultHandlerSync) {
                                    if (done.get()) return;
                                    if (record != null) {
                                        lookupRecords.add(record)
                                        if (record.type == Type.A)
                                            haveV4.set(true)
                                        if (record.type == Type.AAAA)
                                            haveV6.set(true)
                                    }
                                }
                            }

                            override fun handleException(id: Any?, e: java.lang.Exception?) {
                                e?.printStackTrace()
                            }
                        }

                        val v4Lookup = Lookup(name, Type.A, DClass.IN)
                        val v6Lookup = Lookup(name, Type.AAAA, DClass.IN)
                        for (i in 1..10) {
                            v4Lookup.lookupRecordsAsync(resolver)
                            v6Lookup.lookupRecordsAsync(resolver)
                        }

                        // Wait up to 5s and at least 200ms longer than it takes
                        // for the first results of each type
                        for (i in 1..50) {
                            if (haveV4.get() && haveV6.get()) break;
                            delay(100)
                        }
                        delay(200)

                        // These are actually no-ops in Lookup hence the additional done atomic
                        v4Lookup.close()
                        v6Lookup.close()

                        synchronized(resultHandlerSync) {
                            done.set(true)
                        }
                    }
                    it.release()
                }
                for (record in lookupRecords) {
                    when (record.type) {
                        Type.AAAA -> {
                            (record as AAAARecord).address.hostAddress?.also { address ->
                                mutableList += MainDataClass(record.name.toString(), Type.AAAA, address)
                            }
                        }

                        Type.A -> {
                            (record as ARecord).address.hostAddress?.also { address ->
                                mutableList += MainDataClass(record.name.toString(), Type.A, address)
                            }
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