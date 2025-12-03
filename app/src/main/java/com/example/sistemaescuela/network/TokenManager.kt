package com.example.sistemaescuela.network

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object {
        private const val KEY_MATRICULA = "user_matricula" // Cambiado a KEY_MATRICULA
        private const val KEY_STUDENT_ID = "student_id"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_CHILDREN_LIST = "children_list_json"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveSession(token: String, name: String, role: String, id: String, studentId: String?, matricula: String?) {
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_ROLE, role)
        editor.putString(KEY_USER_ID, id)

        if (studentId != null) editor.putString(KEY_STUDENT_ID, studentId)
        if (matricula != null) editor.putString(KEY_MATRICULA, matricula) //  Guardar

        editor.apply()
    }
    fun getStudentId(): String? {
        return prefs.getString(KEY_STUDENT_ID, null)
    }

    //  Función para guardar la lista de hijos (como JSON)
    fun saveChildrenList(hijos: List<com.example.sistemaescuela.network.HijoVinculo>?) {
        if (hijos != null) {
            val json = com.google.gson.Gson().toJson(hijos)
            editor.putString(KEY_CHILDREN_LIST, json)
        }
        editor.apply()
    }

    // Función para recuperar la lista (JSON)
    fun getChildrenListJson(): String? {
        return prefs.getString(KEY_CHILDREN_LIST, null)
    }

    //  Función para guardar el ID del hijo seleccionado (usa la misma llave que el estudiante normal)
    fun saveSelectedStudentId(studentId: String) {
        editor.putString(KEY_STUDENT_ID, studentId)
        editor.apply()
    }
    fun getMatricula(): String? {
        return prefs.getString(KEY_MATRICULA, null)
    }
    fun getUserRole(): String? {
        return prefs.getString(KEY_USER_ROLE, null)
    }
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}