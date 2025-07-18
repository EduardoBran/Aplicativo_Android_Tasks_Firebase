package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

// * Modela todos os estados possíveis que a tela de autenticação pode exibir.
// * Usamos um sealed class para garantir que tratemos exaustivamente cada caso na UI.

sealed class AuthState {

    object Idle: AuthState()                                // Sem ação em andamento
    object Loading: AuthState()                             // Operação em progresso
    object Success : AuthState()                            // Operação concluída com sucesso
    data class Error(val message: String) : AuthState()     // Falha, com mensagem de erro
}