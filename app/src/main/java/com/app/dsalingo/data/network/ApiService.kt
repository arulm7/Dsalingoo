package com.app.dsalingo.data.network

import com.app.dsalingo.data.model.Question
import retrofit2.http.*

interface ApiService {

    // 1. Authentication
    @POST("api/auth.php?action=register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("api/auth.php?action=login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    // 2. Categories & Learn Progress
    @GET("api/learn.php")
    suspend fun getCategories(
        @Query("user_id") userId: Int
    ): CategoriesResponse

    // 3. Lessons & Questions
    @GET("api/questions.php")
    suspend fun getQuestionsForLesson(
        @Query("category_id") categoryId: String
    ): List<Question>

    // 4. User stats updates
    @POST("api/user.php?action=update_stats")
    suspend fun updateStats(
        @Body request: UpdateStatsRequest
    ): StatsUpdateResponse

    @POST("api/user.php?action=complete_question")
    suspend fun completeQuestion(
        @Body request: CompleteQuestionRequest
    ): StatsUpdateResponse



    @GET("api/user.php?action=profile")
    suspend fun getUserProfile(
        @Query("user_id") userId: Int
    ): StatsUpdateResponse

    // 5. Leaderboard
    @GET("api/leaderboard.php")
    suspend fun getLeaderboard(): LeaderboardResponse

    // 6. Curated Challenges
    @GET("api/challenges.php")
    suspend fun getChallenges(
        @Query("user_id") userId: Int
    ): ChallengesResponse

    @POST("api/challenges.php?action=complete")
    suspend fun completeChallenge(
        @Body request: CompleteChallengeRequest
    ): CompleteChallengeResponse

    // 7. Admin CRUD Services
    @GET("api/admin.php?action=stats")
    suspend fun getAdminStats(): AdminStatsResponse

    @POST("api/admin.php?action=add_question")
    suspend fun addQuestion(
        @Body request: AdminQuestionRequest
    ): AdminActionResponse

    @POST("api/admin.php?action=edit_question")
    suspend fun editQuestion(
        @Body request: AdminQuestionRequest
    ): AdminActionResponse

    @POST("api/admin.php?action=delete_question")
    suspend fun deleteQuestion(
        @Body request: AdminDeleteRequest
    ): AdminActionResponse
}
