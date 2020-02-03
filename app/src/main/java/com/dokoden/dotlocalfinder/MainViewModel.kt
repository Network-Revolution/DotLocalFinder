package com.dokoden.dotlocalfinder

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.posick.mdns.Lookup
import org.xbill.DNS.AAAARecord
import org.xbill.DNS.ARecord
import org.xbill.DNS.DClass
import org.xbill.DNS.Type

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val wifi = application.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val liveDataList = MutableLiveData<List<MainDataClass>>()
    val localName = ObservableField("")
    val ipVersionList = listOf("[IPv6]", "IPv6", "IPv4")
    var selectedPosition = 0

    fun onResolve() {
        val resolveIpVersion = ipVersionList[selectedPosition]
        val mutableList = mutableListOf<MainDataClass>()
        Thread(Runnable {
            try {
                when (resolveIpVersion) {
                    "[IPv6]", "IPv6" -> {
                        wifi.createMulticastLock("mDnsLock4IPv6").apply {
                            setReferenceCounted(true)
                            acquire()
                            for (record in Lookup(localName.get(), Type.AAAA, DClass.IN).lookupRecords()) {
                                if (record.type == Type.AAAA) {
                                    mutableList += MainDataClass(
                                        localName.get()!!,
                                        resolveIpVersion,
                                        (record as AAAARecord).address.hostAddress
                                    )
                                }
                            }
                            release()
                        }
                    }
                    "IPv4" -> {
                        wifi.createMulticastLock("mDnsLock4IPv4").apply {
                            setReferenceCounted(true)
                            acquire()
                            for (record in Lookup(localName.get(), Type.A, DClass.IN).lookupRecords()) {
                                if (record.type == Type.A) {
                                    mutableList += MainDataClass(
                                        localName.get()!!,
                                        resolveIpVersion,
                                        (record as ARecord).address.hostAddress
                                    )
                                }
                            }
                            release()
                        }
                    }
                }
                liveDataList.postValue(mutableList.distinct())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }
}