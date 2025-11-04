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
import com.example.sistemaescuela.ui.screens.LoginScreen
import com.example.sistemaescuela.ui.screens.MainMenuScreen
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
                        // This removes the login screen from the back stack
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main_menu") {
            MainMenuScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SistemaEscuelaTheme {
        // Preview the LoginScreen as it's the entry point
        LoginScreen(onLoginClick = {})
    }
}
