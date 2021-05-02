package com.crazydevs.community.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.crazydevs.community.MainActivity
import com.crazydevs.community.UserViewModel
import com.crazydevs.community.base.BaseFragment
import com.crazydevs.community.databinding.FragmentLoginBinding
import com.crazydevs.community.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Login fragment.
 */
class LoginFragment : BaseFragment() {

    private val userViewModel by activityViewModels<UserViewModel>()

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(inflater, container, false).also {
        binding = it
        firebaseAuth = FirebaseAuth.getInstance()
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)
        observeLogin()

        with(binding) {
            loginButton.setOnClickListener {
                userViewModel.signInWithEmailAndPassword(
                    firebaseAuth, emailIdEditText.text.toString(),
                    pwdEditText.text.toString()
                )
            }
        }
    }

    private fun observeLogin() {
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> showOrHideProgress(true)
                is Resource.Success -> {
                    showOrHideProgress(false)
                    updateUI(it.data?.currentUser)
                }
                is Resource.Failure -> {
                    when (it.error) {
                        ACTION_VALIDATION_FAILED -> {
                            Toast.makeText(
                                context,
                                "Validation Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        ACTION_AUTHENTICATION_FAILED -> {
                            Toast.makeText(
                                context,
                                "Authentication Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    showOrHideProgress(false)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.currentUser?.let {
            userViewModel.saveCachedUser(firebaseAuth)
            updateUI(it)
        } ?: reload()
    }

    private fun showOrHideProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun reload() {
        userViewModel.reloadUser(firebaseAuth)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            savedStateHandle.set(LOGIN_SUCCESSFUL, true)
            findNavController().popBackStack()
        }
    }

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
        const val ACTION_VALIDATION_FAILED = "actionValidationFailed"
        const val ACTION_AUTHENTICATION_FAILED = "actionAuthenticationFailed"
    }
}