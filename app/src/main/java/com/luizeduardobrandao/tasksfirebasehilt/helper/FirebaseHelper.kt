package com.luizeduardobrandao.tasksfirebasehilt.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebasehilt.R

class FirebaseHelper {

    companion object {

        fun getAuth() = FirebaseAuth.getInstance()
        fun getDatabase() = Firebase.database.reference

        // Recupera ID do usuário
        fun getIdUser() = getAuth().currentUser?.uid ?: ""

        // Verificar Autenticação (booleano)
        fun isAutenticated() = getAuth().currentUser != null

        // Recebe as mensagens do Firebase e traduz
        fun validError(error: String): Int {
            return when {
                error.contains("The supplied auth credential is") -> {
                    R.string.account_invalid_login_fragment
                }
                error.contains("The email address is already in use") -> {
                    R.string.email_in_use_register_fragment
                }
                error.contains("Password should be at least 6 characters") -> {
                    R.string.strong_password_register_fragment
                }
                else -> {
                    R.string.text_generic_error
                }
            }
        }
    }
}