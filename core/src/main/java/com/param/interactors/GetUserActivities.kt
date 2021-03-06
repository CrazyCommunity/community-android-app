package com.param.interactors

import com.param.data.UserDataRepository

class GetUserActivities(private val userDataRepository: UserDataRepository) {
    suspend operator fun invoke(userId: String) = userDataRepository.getUserActivity(userId)
}