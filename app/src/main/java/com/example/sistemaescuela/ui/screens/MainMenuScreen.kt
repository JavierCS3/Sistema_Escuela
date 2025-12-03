
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

enum class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    PROFILE("profile", "Perfil", Icons.Default.Home),
    ACADEMIC_PROGRESS("progress", "Progreso", Icons.Default.Assessment),
    TASKS("tasks", "Tareas", Icons.Default.Task), // Esta es la pestaña (Muestra Materias)
    MESSAGING("messaging", "Mensajes", Icons.AutoMirrored.Filled.Chat),
    ALERTS("alerts", "Alertas"),
    SETTINGS("settings", "Configuración"),
    CHAT("chat/{contactName}", "Chat"),
    TASK_DETAIL("task_detail/{title}/{desc}/{content}/{status}/{reqAval}/{entregaId}", "Detalle"),

    // --- NUEVAS RUTAS ---
    TASK_LIST("task_list/{materiaId}", "Lista de Tareas"),
    SUBMIT_TASK("submit_task/{tareaId}/{titulo}", "Entregar Tarea")
}

const val CONTACT_NAME_ARG = "contactName"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onLogoutClick: () -> Unit //
) {
    val navController = rememberNavController()
    var menuExpanded by remember { mutableStateOf(false) }

    val bottomNavScreens = listOf(Screen.PROFILE, Screen.ACADEMIC_PROGRESS, Screen.TASKS, Screen.MESSAGING)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = Screen.values().find {
        val routePattern = it.route.substringBefore("/")
        currentDestination?.route?.startsWith(routePattern) == true
    } ?: Screen.PROFILE

    // Es pantalla principal si está en la lista de bottomNavScreens
    val isTopLevelScreen = bottomNavScreens.any { it.route == currentDestination?.route }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (currentScreen == Screen.CHAT) {
                        val contactName = navBackStackEntry?.arguments?.getString(CONTACT_NAME_ARG) ?: ""
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp))
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
                    // El menú de opciones solo aparece en pantallas principales
                    if (isTopLevelScreen) {
                        IconButton(onClick = { navController.navigate(Screen.ALERTS.route) }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alertas")
                        }
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
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
                                        onLogoutClick() // Ahora sí funciona
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
                            label = { Text(screen.label) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.PROFILE.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.PROFILE.route) { StudentProfileScreen() }

            composable(Screen.ACADEMIC_PROGRESS.route) { AcademicProgressScreen() }

            // --- FLUJO DE TAREAS CORREGIDO ---
            //
            composable(Screen.TASKS.route) {
                SubjectsScreen(
                    onMateriaClick = { materiaId ->
                        // Al dar clic en materia, vamos a la lista de tareas
                        navController.navigate("task_list/$materiaId")
                    }
                )
            }

            // 2. Pantalla de Tareas específica (recibe el ID)
            composable(
                route = Screen.TASK_LIST.route,
                arguments = listOf(navArgument("materiaId") { type = NavType.StringType })
            ) { backStackEntry ->
                val materiaId = backStackEntry.arguments?.getString("materiaId") ?: ""

                TasksScreen(
                    materiaId = materiaId,
                    onTaskClick = { taskId, titulo ->
                        navController.navigate("submit_task/$taskId/$titulo")
                    },
                    // Actualizamos el callback
                    onViewDetailClick = { titulo, desc, contenido, estado, reqAval, entregaId, archivoUrl ->

                        val encTitulo = java.net.URLEncoder.encode(titulo, "UTF-8")
                        val encDesc = java.net.URLEncoder.encode(desc, "UTF-8")

                        // Contenido seguro
                        val contenidoSeguro = if (contenido.isNullOrBlank()) "SIN_CONTENIDO" else contenido
                        val encCont = java.net.URLEncoder.encode(contenidoSeguro, "UTF-8")

                        val idSeguro = entregaId ?: "null"

                        // --- NUEVO: URL SEGURA ---
                        // Si no hay link, mandamos "SIN_URL". Si hay, lo codificamos (importante por los slashes /)
                        val urlSegura = if (archivoUrl.isNullOrBlank()) "SIN_URL" else java.net.URLEncoder.encode(archivoUrl, "UTF-8")
                        // -------------------------

                        // Agregamos la urlSegura al final de la ruta
                        navController.navigate("task_detail/$encTitulo/$encDesc/$encCont/$estado/$reqAval/$idSeguro/$urlSegura")
                    }
                )
            }


            composable(
                // Agregamos /{fileUrl} al final de la ruta
                route = "task_detail/{title}/{desc}/{content}/{status}/{reqAval}/{entregaId}/{fileUrl}",
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("desc") { type = NavType.StringType },
                    navArgument("content") { type = NavType.StringType },
                    navArgument("status") { type = NavType.StringType },
                    navArgument("reqAval") { type = NavType.BoolType },
                    navArgument("entregaId") { type = NavType.StringType },
                    navArgument("fileUrl") { type = NavType.StringType } // <--- Nuevo argumento
                )
            ) { entry ->
                TaskDetailScreen(
                    titulo = entry.arguments?.getString("title") ?: "",
                    descripcion = entry.arguments?.getString("desc") ?: "",
                    contenidoEntrega = entry.arguments?.getString("content") ?: "",
                    estado = entry.arguments?.getString("status") ?: "",
                    requiereAval = entry.arguments?.getBoolean("reqAval") ?: false,
                    entregaId = entry.arguments?.getString("entregaId").takeIf { it != "null" },
                    // Pasamos la URL a la pantalla
                    archivoUrl = entry.arguments?.getString("fileUrl"),
                    onNavigateUp = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.SUBMIT_TASK.route,
                arguments = listOf(
                    navArgument("tareaId") { type = NavType.StringType },
                    navArgument("titulo") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tareaId = backStackEntry.arguments?.getString("tareaId") ?: ""
                val titulo = backStackEntry.arguments?.getString("titulo") ?: "Tarea"

                SubmitTaskScreen(
                    tareaId = tareaId,
                    tituloTarea = titulo,
                    onNavigateUp = { navController.popBackStack() },
                    onSuccess = {

                        navController.popBackStack()
                    }
                )
            }

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
