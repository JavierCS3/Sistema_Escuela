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
import com.example.sistemaescuela.network.TokenManager
import com.example.sistemaescuela.ui.theme.SistemaEscuelaTheme
import androidx.compose.ui.platform.LocalContext

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
            val context = LocalContext.current
            val tokenManager = TokenManager(context)
            LoginScreen(
                onLoginClick = {
                    // LEER EL ROL PARA SABER A DÓNDE IR
                    val rol = tokenManager.getUserRole()
                    if (rol == "Padre de Familia") {
                        navController.navigate("child_selection") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("main_menu") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onActivateAccountClick = { /* ... */ },
                onForgotPasswordClick = { /* ... */ }
            )
        }
        composable("child_selection") {
            ChildSelectionScreen(
                onChildSelected = {
                    navController.navigate("main_menu") {
                        popUpTo("child_selection") { inclusive = true }
                    }
                }
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
        composable(
            route = "tasks/{materiaId}",
            arguments = listOf(navArgument("materiaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val materiaId = backStackEntry.arguments?.getString("materiaId") ?: ""

            TasksScreen(
                materiaId = materiaId,
                onTaskClick = { taskId, titulo ->
                    navController.navigate("submit_task/$taskId/$titulo")
                },
                // --- CORRECCIÓN AQUÍ ---
                // Agregamos 'archivoUrl' al final de la lista de parámetros (Ahora son 7)
                onViewDetailClick = { titulo, desc, contenido, estado, reqAval, entregaId, archivoUrl ->

                    val encTitulo = java.net.URLEncoder.encode(titulo, "UTF-8")
                    val encDesc = java.net.URLEncoder.encode(desc, "UTF-8")

                    val contenidoSeguro = if (contenido.isNullOrBlank()) "SIN_CONTENIDO" else contenido
                    val encCont = java.net.URLEncoder.encode(contenidoSeguro, "UTF-8")

                    val idSeguro = entregaId ?: "null"

                    // --- NUEVO: Procesar la URL del archivo ---
                    val urlSegura = if (archivoUrl.isNullOrBlank()) "SIN_URL" else java.net.URLEncoder.encode(archivoUrl, "UTF-8")

                    // --- NUEVO: Agregamos urlSegura al final de la ruta ---
                    navController.navigate("task_detail/$encTitulo/$encDesc/$encCont/$estado/$reqAval/$idSeguro/$urlSegura")
                }
            )
        }

        composable(
            route = "submit_task/{tareaId}/{titulo}",
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
                    // Al tener éxito, volvemos atrás para que se actualice la lista
                    navController.popBackStack()
                }
            )
        }
        composable(
            // 1. Agregamos /{fileUrl} al final de la ruta
            route = "task_detail/{title}/{desc}/{content}/{status}/{reqAval}/{entregaId}/{fileUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("desc") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("status") { type = NavType.StringType },
                navArgument("reqAval") { type = NavType.BoolType },
                navArgument("entregaId") { type = NavType.StringType },
                // 2. Agregamos el argumento a la lista
                navArgument("fileUrl") { type = NavType.StringType }
            )
        ) { entry ->
            TaskDetailScreen(
                titulo = entry.arguments?.getString("title") ?: "",
                descripcion = entry.arguments?.getString("desc") ?: "",
                contenidoEntrega = entry.arguments?.getString("content") ?: "",
                estado = entry.arguments?.getString("status") ?: "",
                requiereAval = entry.arguments?.getBoolean("reqAval") ?: false,
                entregaId = entry.arguments?.getString("entregaId").takeIf { it != "null" },
                // 3. Pasamos el valor a la pantalla
                archivoUrl = entry.arguments?.getString("fileUrl"),
                onNavigateUp = { navController.popBackStack() }
            )
        }
    }
}