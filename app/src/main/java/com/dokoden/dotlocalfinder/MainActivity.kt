package com.dokoden.dotlocalfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dokoden.dotlocalfinder.databinding.MainActivityBinding
import net.taptappun.taku.kobayashi.runtimepermissionchecker.RuntimePermissionChecker

class MainActivity : AppCompatActivity() {

    private val requestPermission = 100
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //パーミッションの許可
        RuntimePermissionChecker.requestAllPermissions(this, requestPermission)

        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
    }
}
