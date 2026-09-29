package com.app.dsalingo.data.repository

import com.app.dsalingo.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAdminStats(): AdminStatsResponse? {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAdminStats()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun addQuestion(request: AdminQuestionRequest): AdminActionResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.addQuestion(request)
            } catch (e: Exception) {
                e.printStackTrace()
                AdminActionResponse("error", "Network connection failed: ${e.message}")
            }
        }
    }

    suspend fun editQuestion(request: AdminQuestionRequest): AdminActionResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.editQuestion(request)
            } catch (e: Exception) {
                e.printStackTrace()
                AdminActionResponse("error", "Network connection failed: ${e.message}")
            }
        }
    }

    suspend fun deleteQuestion(id: String): AdminActionResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.deleteQuestion(AdminDeleteRequest(id))
            } catch (e: Exception) {
                e.printStackTrace()
                AdminActionResponse("error", "Network connection failed: ${e.message}")
            }
        }
    }
}
