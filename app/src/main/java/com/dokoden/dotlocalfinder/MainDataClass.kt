package com.dokoden.dotlocalfinder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

data class MainDataClass(val mDnsName: String, val resolveType: String, val ipAddress: String) {

    fun onClickButton(view: View) {

        val copyAddress = when (resolveType) {
            "[IPv6]" -> "[$ipAddress]"
            else -> ipAddress
        }

        val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("mDNS", copyAddress))
        // clipboard.primaryClip = ClipData.newPlainText("mDNS", copyAddress)
        Snackbar.make(view, "Copied the IP address : $copyAddress", Snackbar.LENGTH_SHORT).show()
    }
}
