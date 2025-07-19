package com.luizeduardobrandao.tasksfirebasehilt.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.luizeduardobrandao.tasksfirebasehilt.R

class FirebaseHelper {

    companion object {

        // Recebe as mensagens do Firebase e traduz
        fun validError(error: String): Int {
            return when {
                error.contains("The supplied auth credential is") -> {
                    R.string.account_invalid_login_fragment
                }
                error.contains("The email address is badly") -> {
                    R.string.text_login_email_error
                }
                error.contains("The given password") -> {
                    R.string.text_password_min_length_error
                }
                error.contains("Given String is") -> {
                    R.string.fields_empty
                }
                else -> {
                    R.string.text_generic_error
                }
            }
        }
    }
}