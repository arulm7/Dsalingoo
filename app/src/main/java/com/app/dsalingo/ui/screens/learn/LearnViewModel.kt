package com.app.dsalingo.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dsalingo.data.model.DataStructureCategory
import com.app.dsalingo.data.repository.UserRepository
import com.app.dsalingo.ui.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<DataStructureCategory>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<DataStructureCategory>>> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<DataStructureCategory>>(emptyList())
    val categories: StateFlow<List<DataStructureCategory>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = UiState.Loading
            try {
                val list = userRepository.getCategories()
                _categories.value = list
                if (list.isNotEmpty()) {
                    _uiState.value = UiState.Success(list)
                } else {
                    _uiState.value = UiState.Success(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error(
                    message = e.localizedMessage ?: "Failed to connect to the learning server. Please check your network."
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
