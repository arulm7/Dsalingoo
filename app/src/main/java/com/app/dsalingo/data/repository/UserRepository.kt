package com.app.dsalingo.data.repository

import com.app.dsalingo.data.manager.HeartManager
import com.app.dsalingo.data.model.User
import com.app.dsalingo.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val heartManager: HeartManager
) {
    private val _currentUser = MutableStateFlow<User?>(User(id = "1", username = "dsa_wizard", email = "wizard@dsalingo.com", hearts = heartManager.hearts.value))
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun login(request: LoginRequest): AuthResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(request)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                    heartManager.syncWithUser(response.user)
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                AuthResponse("error", "Login failed: ${e.message}", null)
            }
        }
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(request)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                    heartManager.syncWithUser(response.user)
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                AuthResponse("error", "Registration failed: ${e.message}", null)
            }
        }
    }

    suspend fun updateStats(xpGain: Int): StatsUpdateResponse? {
        val user = _currentUser.value ?: return null
        return withContext(Dispatchers.IO) {
            try {
                val request = UpdateStatsRequest(userId = user.id.toIntOrNull() ?: 1, xpGain = xpGain)
                val response = apiService.updateStats(request)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun completeQuestion(questionId: String): StatsUpdateResponse? {
        val user = _currentUser.value ?: return null
        return withContext(Dispatchers.IO) {
            try {
                val request = CompleteQuestionRequest(userId = user.id.toIntOrNull() ?: 1, questionId = questionId)
                val response = apiService.completeQuestion(request)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun fetchProfile(): Boolean {
        val user = _currentUser.value ?: return false
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserProfile(user.id.toIntOrNull() ?: 1)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                    heartManager.syncWithUser(response.user)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getCategories(): List<com.app.dsalingo.data.model.DataStructureCategory> {
        val user = _currentUser.value
        val userId = user?.id?.toIntOrNull() ?: 1
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCategories(userId)
                if (response.status == "success") {
                    response.categories
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
