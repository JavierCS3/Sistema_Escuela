package com.example.sistemaescuela.network

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object {
        private const val KEY_STUDENT_ID = "student_id"
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveSession(token: String, name: String, role: String, id: String, studentId: String?) {
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_USER_NAME, name)
        editor.putString(KEY_USER_ROLE, role)
        editor.putString(KEY_USER_ID, id)
        // Guardamos el ID de estudiante si existe
        if (studentId != null) {
            editor.putString(KEY_STUDENT_ID, studentId)
        }
        editor.apply()
    }
    fun getStudentId(): String? {
        return prefs.getString(KEY_STUDENT_ID, null)
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