package com.app.dsalingo.data.network

import com.app.dsalingo.data.model.*
import com.google.gson.annotations.SerializedName

// 1. Auth Requests and Responses
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val user: User?
)

// 2. Categories
data class CategoriesResponse(
    val status: String,
    val categories: List<DataStructureCategory>
)

// 3. User stats updates
data class UpdateStatsRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("xp_gain") val xpGain: Int,
    val hearts: Int? = null,
    val streak: Int? = null,
    val level: Int? = null,
    val crowns: Int? = null
)

data class CompleteQuestionRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("question_id") val questionId: String
)



data class StatsUpdateResponse(
    val status: String,
    val message: String,
    val unlockedAchievements: List<String> = emptyList(),
    val user: User?
)

// 4. Leaderboard
data class LeaderboardResponse(
    val status: String,
    val leaderboard: List<LeaderboardUser>
)

// 5. Challenges
data class ChallengesResponse(
    val status: String,
    val challenges: List<Challenge>
)

data class CompleteChallengeRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("challenge_id") val challengeId: String
)

data class CompleteChallengeResponse(
    val status: String,
    val message: String,
    @SerializedName("xpEarned") val xpEarned: Int = 0,
    val unlockedAchievements: List<String> = emptyList()
)

// 6. Admin Panel Requests and Responses
data class AdminStatsResponse(
    val status: String,
    val totalUsers: Int,
    val totalQuestions: Int,
    val totalChallenges: Int
)

data class AdminQuestionRequest(
    val id: String,
    @SerializedName("category_id") val categoryId: String,
    val type: String,
    val question: String,
    val options: List<String>? = null,
    @SerializedName("correct_answer") val correctAnswer: Any, // Can be Int index, String, or List
    val explanation: String,
    val code: String? = null,
    val blanks: List<String>? = null,
    val items: List<String>? = null,
    @SerializedName("correct_order") val correctOrder: List<Int>? = null,
    @SerializedName("array_data") val arrayData: List<String>? = null,
    @SerializedName("image_url") val imageUrl: String? = null
)

data class AdminDeleteRequest(
    val id: String
)

data class AdminActionResponse(
    val status: String,
    val message: String
)
