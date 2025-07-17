package com.luizeduardobrandao.tasksfirebasehilt.domain.repository

// * Define as operações de autenticação disponíveis na aplicação.
// * Desacopla a camada de domínio das implementações concretas do Firebase.

interface AuthRepository {

    // Faz login com email/senha
    suspend fun login (email: String, password: String): Result<Unit>

    // Registrar um novo usuário
    suspend fun register (email: String, password: String): Result<Unit>

    // Envia email de recuperação de senha
    suspend fun recoverAccount(email: String): Result<Unit>

    // Desloga o usuário atual
    fun logout()

    // Verifica se já existe um usuário autenticado
    fun isAuthenticated(): Boolean
}