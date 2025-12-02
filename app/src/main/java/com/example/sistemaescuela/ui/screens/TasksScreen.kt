package com.example.sistemaescuela.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Data classes for structure ---
data class Task(val id: Int, val title: String, val subject: String, var isCompleted: Boolean)
data class TaskGroup(val title: String, val tasks: List<Task>)

// --- Dummy Data ---
val taskGroups = listOf(
    TaskGroup(
        "Hoy (2)", tasks = listOf(
            Task(1, "Terminar reporte de lectura", "Literatura", false),
            Task(2, "Resolver guía de cálculo", "Matemáticas", false)
        )
    ),
    TaskGroup(
        "Mañana (1)", tasks = listOf(
            Task(3, "Practicar presentación", "Oratoria", false)
        )
    ),
    TaskGroup(
        "Domingo (3)", tasks = listOf(
            Task(4, "Estudiar para el examen", "Física", false),
            Task(5, "Hacer boceto de proyecto", "Arte", false),
            Task(6, "Leer capítulo 5", "Historia", false)
        )
    )
)

@Composable
fun TasksScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Add new task action */ }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Tarea")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.padding(8.dp)) }

            taskGroups.forEach { group ->
                item {
                    TaskGroupHeader(group)
                }
            }
        }
    }
}

@Composable
fun TaskGroupHeader(group: TaskGroup) {
    var isExpanded by remember { mutableStateOf(group.title.startsWith("Hoy")) } // Expand "Hoy" by default

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(group.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Contraer" else "Expandir"
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
                group.tasks.forEach { task ->
                    TaskItem(task = task)
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    var isChecked by remember { mutableStateOf(task.isCompleted) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    task.isCompleted = it
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = task.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = task.subject, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    TasksScreen()
}
