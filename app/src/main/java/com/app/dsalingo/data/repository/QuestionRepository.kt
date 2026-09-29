package com.app.dsalingo.data.repository

import com.app.dsalingo.data.model.Question
import com.app.dsalingo.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getQuestionsForLesson(categoryId: String): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getQuestionsForLesson(categoryId)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
