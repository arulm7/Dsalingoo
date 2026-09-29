package com.app.dsalingo.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.dsalingo.ui.screens.auth.LoginScreen
import com.app.dsalingo.ui.screens.auth.SignupScreen
import com.app.dsalingo.ui.screens.challenges.ChallengesScreen
import com.app.dsalingo.ui.screens.dashboard.DashboardScreen
import com.app.dsalingo.ui.screens.landing.LandingScreen
import com.app.dsalingo.ui.screens.leaderboard.LeaderboardScreen
import com.app.dsalingo.ui.screens.learn.CategoryDetailScreen
import com.app.dsalingo.ui.screens.learn.LearnScreen
import com.app.dsalingo.ui.screens.lesson.LessonDetailScreen
import com.app.dsalingo.ui.screens.profile.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Landing.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onLoginSuccess = { isAdmin ->
                    val destination = if (isAdmin) Screen.AdminDashboard.route else Screen.Dashboard.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onSignupSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Landing.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToLearn = { navController.navigate(Screen.Learn.route) },
                onNavigateToChallenges = { navController.navigate(Screen.Challenges.route) }
            )
        }

        composable(Screen.Learn.route) {
            LearnScreen(
                onNavigateToCategory = { categoryId ->
                    navController.navigate(Screen.CategoryDetail.createRoute(categoryId))
                }
            )
        }

        composable(Screen.CategoryDetail.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            CategoryDetailScreen(
                categoryId = categoryId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLesson = { catId, lessonId ->
                    navController.navigate(Screen.LessonDetail.createRoute(catId, lessonId))
                }
            )
        }

        composable(Screen.LessonDetail.route) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            LessonDetailScreen(
                categoryId = categoryId,
                lessonId = lessonId,
                onNavigateBack = { navController.popBackStack() },
                onLessonComplete = { navController.popBackStack() }
            )
        }

        composable(Screen.Challenges.route) {
            ChallengesScreen()
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onSignOut = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            com.app.dsalingo.ui.screens.admin.AdminDashboardScreen(
                onSignOut = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
