package com.app.dsalingo.ui.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Learn : Screen("learn")
    object CategoryDetail : Screen("learn/{categoryId}") {
        fun createRoute(categoryId: String) = "learn/$categoryId"
    }
    object LessonDetail : Screen("learn/{categoryId}/{lessonId}") {
        fun createRoute(categoryId: String, lessonId: String) = "learn/$categoryId/$lessonId"
    }
    object Challenges : Screen("challenges")
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
    object AdminDashboard : Screen("admin_dashboard")
}
