package com.crazydevs.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.crazydevs.community.databinding.ActivityMainBinding
import com.crazydevs.community.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun FragmentManager.getCurrentNavigationFragment(): Fragment? = primaryNavigationFragment?.childFragmentManager?.fragments?.first()
    private fun getCurrentVisibleFragment() = supportFragmentManager.getCurrentNavigationFragment()

    override fun onBackPressed() {
        if (getCurrentVisibleFragment() is LoginFragment) {
            finish()
        } else
            super.onBackPressed()
    }
}
