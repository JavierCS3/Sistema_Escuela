package com.example.sistemaescuela.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

// Enum labels updated for brevity
enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    PROFILE("profile", "Perfil", Icons.Default.Home),
    ACADEMIC_PROGRESS("progress", "Progreso", Icons.Default.Assessment),
    TASKS("tasks", "Tareas", Icons.Default.Task),
    MESSAGING("messaging", "Mensajes", Icons.AutoMirrored.Filled.Chat),
    ALERTS("alerts", "Alertas"),
    SETTINGS("settings", "Configuración"),
    CHAT("chat/{contactName}", "Chat")
}

const val CONTACT_NAME_ARG = "contactName"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(onLogoutClick: () -> Unit) {
    val navController = rememberNavController()
    var menuExpanded by remember { mutableStateOf(false) }

    val bottomNavScreens = listOf(Screen.PROFILE, Screen.ACADEMIC_PROGRESS, Screen.TASKS, Screen.MESSAGING)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreenRoute = currentDestination?.route
    val currentScreen = Screen.values().find {
        val routePattern = it.route.substringBefore("/")
        currentScreenRoute?.startsWith(routePattern) == true
    } ?: Screen.PROFILE

    val isTopLevelScreen = bottomNavScreens.any { it.route == currentDestination?.route }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentScreen.route.startsWith("chat")) {
                        val contactName = navBackStackEntry?.arguments?.getString(CONTACT_NAME_ARG) ?: ""
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = "Avatar", modifier = Modifier.size(32.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(contactName)
                        }
                    } else {
                        Text(currentScreen.label)
                    }
                },
                navigationIcon = {
                    if (!isTopLevelScreen) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    if (isTopLevelScreen) {
                        IconButton(onClick = { navController.navigate(Screen.ALERTS.route) }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alertas")
                        }
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                            }
                            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Configuración") },
                                    leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                    onClick = {
                                        navController.navigate(Screen.SETTINGS.route)
                                        menuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cerrar Sesión") },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                                    onClick = {
                                        menuExpanded = false
                                        onLogoutClick()
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (isTopLevelScreen) {
                NavigationBar {
                    bottomNavScreens.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.label) },
                            label = { Text(screen.label, textAlign = TextAlign.Center) },
                            selected = isSelected,
                            onClick = { navController.navigate(screen.route) { launchSingleTop = true } },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.secondaryContainer)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.PROFILE.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.PROFILE.route) { StudentProfileScreen() }
            composable(Screen.ACADEMIC_PROGRESS.route) { AcademicProgressScreen() }
            composable(Screen.TASKS.route) { TasksScreen() }
            composable(Screen.MESSAGING.route) {
                MessagingScreen(onContactClick = { contactName -> navController.navigate("chat/$contactName") })
            }
            composable(Screen.ALERTS.route) { AlertsScreen() }
            composable(Screen.SETTINGS.route) { SettingsScreen(onNavigateUp = { navController.popBackStack() }) }
            composable(
                route = Screen.CHAT.route,
                arguments = listOf(navArgument(CONTACT_NAME_ARG) { type = NavType.StringType })
            ) { backStackEntry ->
                val contactName = backStackEntry.arguments?.getString(CONTACT_NAME_ARG)
                ChatScreen(contactName = contactName ?: "Desconocido", onNavigateUp = { navController.popBackStack() })
            }
        }
    }
}
