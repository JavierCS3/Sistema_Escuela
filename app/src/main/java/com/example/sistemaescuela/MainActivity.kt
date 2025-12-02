package com.example.sistemaescuela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
<<<<<<< HEAD
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sistemaescuela.ui.screens.*
=======
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
>>>>>>> 44757816577b2cc3f947e096052036431025394e
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

<<<<<<< HEAD
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
=======



@PreviewScreenSizes
@Composable
fun SistemaEscuelaApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
>>>>>>> 44757816577b2cc3f947e096052036431025394e
            )
        }
    }
}

<<<<<<< HEAD
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SistemaEscuelaTheme {
        LoginScreen(onLoginClick = {}, onActivateAccountClick = {}, onForgotPasswordClick = {})
    }
}
=======
enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SistemaEscuelaTheme {
        Greeting("Android")
    }
}
>>>>>>> 44757816577b2cc3f947e096052036431025394e
