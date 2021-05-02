package com.crazydevs.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crazydevs.community.login.LoginFragment
import com.crazydevs.community.utils.Resource
import com.google.firebase.auth.FirebaseAuth

class UserViewModel: ViewModel() {
    private var mutableUserLiveData = MutableLiveData<Resource<FirebaseAuth?>>()

    val userLiveData: LiveData<Resource<FirebaseAuth?>>
        get() = mutableUserLiveData

    fun logout() {
        mutableUserLiveData.value?.data?.signOut()
        mutableUserLiveData.value = null
    }

    fun signInWithEmailAndPassword(firebaseAuth: FirebaseAuth, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty())
            mutableUserLiveData.value = Resource.Failure(LoginFragment.ACTION_VALIDATION_FAILED)
        else {
            mutableUserLiveData.value = Resource.Loading()
            firebaseAuth.signInWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mutableUserLiveData.value = Resource.Success(firebaseAuth)
                } else {
                    mutableUserLiveData.value = Resource.Failure(LoginFragment.ACTION_AUTHENTICATION_FAILED)
                }
            }
        }
    }

    fun reloadUser(firebaseAuth: FirebaseAuth) {
        firebaseAuth.currentUser?.reload()?.addOnCompleteListener {
            if (it.isSuccessful) {
                mutableUserLiveData.value = Resource.Success(firebaseAuth)
            } else {
                mutableUserLiveData.value = Resource.Failure(LoginFragment.ACTION_AUTHENTICATION_FAILED)
            }
        }
    }

    fun saveCachedUser(firebaseAuth: FirebaseAuth) {
        mutableUserLiveData.value = Resource.Success(firebaseAuth)
    }

    fun checkUserStatus() {
        // This call will trigger logout if the user is already logout
        if (mutableUserLiveData.value == null) {
            mutableUserLiveData.value = null
        }
    }
}