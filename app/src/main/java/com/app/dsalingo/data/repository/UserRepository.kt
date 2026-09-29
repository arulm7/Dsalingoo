package com.app.dsalingo.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.app.dsalingo.data.manager.HeartManager
import com.app.dsalingo.data.model.User
import com.app.dsalingo.data.network.*
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val heartManager: HeartManager
) {
    companion object {
        private const val PREFS_NAME = "dsalingo_user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_JSON = "user_json"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentUser = MutableStateFlow<User?>(loadSavedUser())
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        val user = _currentUser.value
        if (user != null) {
            heartManager.syncWithUser(user)
            scope.launch {
                fetchProfile()
            }
        }
    }

    private fun loadSavedUser(): User? {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return null
        val userJson = prefs.getString(KEY_USER_JSON, null) ?: return null
        return try {
            gson.fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun saveUserToPrefs(user: User?) {
        val editor = prefs.edit()
        if (user != null) {
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
            editor.putString(KEY_USER_JSON, gson.toJson(user))
        } else {
            editor.putBoolean(KEY_IS_LOGGED_IN, false)
            editor.remove(KEY_USER_JSON)
        }
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && _currentUser.value != null
    }

    fun logout() {
        _currentUser.value = null
        saveUserToPrefs(null)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(request)
                if (response.status == "success" && response.user != null) {
                    _currentUser.value = response.user
                    saveUserToPrefs(response.user)
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
                    saveUserToPrefs(response.user)
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
                    saveUserToPrefs(response.user)
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
                    saveUserToPrefs(response.user)
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
                    saveUserToPrefs(response.user)
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
