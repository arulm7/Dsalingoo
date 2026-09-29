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

    suspend fun submitAnswer(
        userId: Int,
        questionId: String,
        answer: Any? = null,
        interactionState: Map<String, Any>? = null
    ): com.app.dsalingo.data.network.QuestionSubmitResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val request = com.app.dsalingo.data.network.QuestionSubmitRequest(
                    userId = userId,
                    questionId = questionId,
                    answer = answer,
                    interactionState = interactionState
                )
                apiService.submitQuestionAnswer(request)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
