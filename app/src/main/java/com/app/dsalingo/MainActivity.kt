package com.app.dsalingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.navigation.compose.rememberNavController
import com.app.dsalingo.ui.navigation.NavGraph
import com.app.dsalingo.ui.theme.DsalingoTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.app.dsalingo.ui.components.MainScaffold

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) } // Placeholder for actual DataStore value
            val systemTheme = isSystemInDarkTheme()
            
            DsalingoTheme(darkTheme = isDarkTheme || systemTheme) {
                val navController = rememberNavController()
                MainScaffold(
                    navController = navController,
                    isDarkTheme = isDarkTheme || systemTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
