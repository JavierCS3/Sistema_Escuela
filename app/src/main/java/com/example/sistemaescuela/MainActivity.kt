package com.example.sistemaescuela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sistemaescuela.ui.screens.*
import com.example.sistemaescuela.ui.theme.SistemaEscuelaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SistemaEscuelaTheme {
                SistemaEscuelaApp()
            }
        }
    }
}

@Composable
fun SistemaEscuelaApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("main_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onActivateAccountClick = { navController.navigate("activation") },
                onForgotPasswordClick = { navController.navigate("forgot_password") }
            )
        }
        composable("activation") {
            ActivationScreen(
                onActivateClick = {
                    navController.navigate("main_menu") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateUp = { navController.popBackStack() }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                onSendLinkClick = { navController.popBackStack() },
                onNavigateUp = { navController.popBackStack() }
            )
        }
        composable("main_menu") {
            MainMenuScreen(
                onLogoutClick = {
                    navController.navigate("login") {
                        // Clear the entire back stack up to the root
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SistemaEscuelaTheme {
        LoginScreen(onLoginClick = {}, onActivateAccountClick = {}, onForgotPasswordClick = {})
    }
}
