package com.param.data

import com.param.domain.Activity
import com.param.domain.User

class UserDataRepository(private val userDataSource: UserDataSource) {
    suspend fun getUserInfo(userId: String) : User {
        return userDataSource.getUserInfo(userId)
    }
    suspend fun getUserActivity(userId: String) : Activity {
        return userDataSource.getUserActivity(userId)
    }
}