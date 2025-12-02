package com.example.sistemaescuela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sistemaescuela.ui.screens.*
import com.example.sistemaescuela.ui.theme.SistemaEscuelaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SistemaEscuelaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("main_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onActivateAccountClick = { /* ... */ },
                onForgotPasswordClick = { /* ... */ }
            )
        }

        composable("main_menu") {
            MainMenuScreen(
                onLogoutClick = {
                    // Al cerrar sesión, volvemos al login y borramos historial
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}