package com.crazydevs.community.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.crazydevs.community.MainActivity
import com.crazydevs.community.R
import com.crazydevs.community.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Login fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding?.loginButton?.setOnClickListener {
            mAuth.signInWithEmailAndPassword(binding?.emailIdEditText?.text.toString(), binding?.pwdEditText?.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mAuth.currentUser?.let {
                                updateUI(it)
                            }
                        } else {
                            Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            updateUI(it)
        } ?: reload()
    }

    private fun reload() {
        mAuth.currentUser?.reload()?.addOnCompleteListener {
            if (it.isSuccessful) {
                updateUI(mAuth.currentUser);
                Toast.makeText(context,
                        "Reload successful!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "reload", it.exception);
                Toast.makeText(context,
                        "Failed to reload user.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            // This will take care of launching home fragment after successful login
            // and remove login fragment from back stack
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment, null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build())
        }
    }

    companion object {
        private val TAG = LoginFragment::class.java.simpleName
    }
}