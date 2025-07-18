package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import javax.inject.Inject

// * Caso de uso para executar o login do usuário.
// * Encapsula a chamada a AuthRepository.login().

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository         // injetado pelo hilt
) {

    // Tenta autenticar o usuário com email/senha.
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.login(email, password)
    }
}