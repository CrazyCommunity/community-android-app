package com.crazydevs.community.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.crazydevs.community.R
import com.crazydevs.community.UserViewModel
import com.crazydevs.community.base.BaseFragment
import com.crazydevs.community.databinding.FragmentHomeBinding
import com.crazydevs.community.login.LoginFragment

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                savedStateHandle.set(LoginFragment.LOGIN_SUCCESSFUL, true)
                Toast.makeText(requireContext(), "User is logged in", Toast.LENGTH_SHORT).show()
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }
        binding.button.setOnClickListener {
            userViewModel.logout()
        }

        userViewModel.checkUserStatus()
    }
}
