package com.app.dsalingo.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dsalingo.data.model.Question
import com.app.dsalingo.data.network.AdminQuestionRequest
import com.app.dsalingo.data.repository.AdminRepository
import com.app.dsalingo.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private val _totalUsers = MutableStateFlow(0)
    val totalUsers: StateFlow<Int> = _totalUsers.asStateFlow()

    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions.asStateFlow()

    private val _totalChallenges = MutableStateFlow(0)
    val totalChallenges: StateFlow<Int> = _totalChallenges.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _actionSuccess = MutableStateFlow<String?>(null)
    val actionSuccess: StateFlow<String?> = _actionSuccess.asStateFlow()

    fun loadDashboardData(categoryId: String = "array") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // 1. Fetch dashboard overview stats
            val stats = adminRepository.getAdminStats()
            if (stats != null && stats.status == "success") {
                _totalUsers.value = stats.totalUsers
                _totalQuestions.value = stats.totalQuestions
                _totalChallenges.value = stats.totalChallenges
            }

            // 2. Fetch active questions inside selected category
            try {
                val list = questionRepository.getQuestionsForLesson(categoryId)
                _questions.value = list
            } catch (e: Exception) {
                _error.value = "Failed to load questions list: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addQuestion(request: AdminQuestionRequest, categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _actionSuccess.value = null
            
            val response = adminRepository.addQuestion(request)
            if (response.status == "success") {
                _actionSuccess.value = "Question '${request.id}' added successfully!"
                loadDashboardData(categoryId)
            } else {
                _error.value = response.message
                _isLoading.value = false
            }
        }
    }

    fun editQuestion(request: AdminQuestionRequest, categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _actionSuccess.value = null

            val response = adminRepository.editQuestion(request)
            if (response.status == "success") {
                _actionSuccess.value = "Question '${request.id}' updated successfully!"
                loadDashboardData(categoryId)
            } else {
                _error.value = response.message
                _isLoading.value = false
            }
        }
    }

    fun deleteQuestion(id: String, categoryId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _actionSuccess.value = null

            val response = adminRepository.deleteQuestion(id)
            if (response.status == "success") {
                _actionSuccess.value = "Question deleted successfully!"
                loadDashboardData(categoryId)
            } else {
                _error.value = response.message
                _isLoading.value = false
            }
        }
    }

    fun clearNotifications() {
        _error.value = null
        _actionSuccess.value = null
    }
}
