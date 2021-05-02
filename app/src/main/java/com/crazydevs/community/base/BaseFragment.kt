package com.crazydevs.community.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.crazydevs.community.MainActivity

abstract class BaseFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    lateinit var savedStateHandle: SavedStateHandle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = (activity as MainActivity)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry
        currentBackStackEntry?.savedStateHandle?.let {
            savedStateHandle = it
        }
    }
}