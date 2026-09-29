package com.app.dsalingo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dsalingo.data.manager.HeartManager
import com.app.dsalingo.data.model.User
import com.app.dsalingo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val heartManager: HeartManager
) : ViewModel() {

    val currentUser: StateFlow<User?> = userRepository.currentUser
    val isLoggedIn: Boolean get() = userRepository.isLoggedIn()
    val hearts: StateFlow<Int> = heartManager.hearts
    val secondsRemaining: StateFlow<Int> = heartManager.secondsRemaining
    val isFull: StateFlow<Boolean> = heartManager.isFull

    fun logout() {
        userRepository.logout()
    }

    fun refreshProfile() {
        viewModelScope.launch {
            userRepository.fetchProfile()
        }
    }

    fun refillOneHeart() {
        heartManager.gainHeart(1)
    }

    fun refillAllHearts() {
        heartManager.refillHearts()
    }

    fun getFormattedTime(): String {
        return heartManager.getFormattedTimeRemaining()
    }

    fun getRechargeProgress(): Float {
        return heartManager.getRechargeProgress()
    }
}
