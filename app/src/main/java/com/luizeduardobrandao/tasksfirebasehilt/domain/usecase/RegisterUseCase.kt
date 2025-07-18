package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import javax.inject.Inject

// * Caso de uso para registrar um novo usuário.
// * Encapsula a chamada a AuthRepository.register().

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    // Cria uma nova conta no FirebaseAuth
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.register(email, password)
    }
}