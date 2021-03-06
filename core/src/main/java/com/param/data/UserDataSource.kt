package com.param.data

import com.param.domain.Activity
import com.param.domain.User

interface UserDataSource {
    suspend fun getUserInfo(userId: String) : User
    suspend fun getUserActivity(userId: String) : Activity
}