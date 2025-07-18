package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import javax.inject.Inject

// * Caso de uso para envio de email de recuperação de senha.
// * Encapsula a chamada a AuthRepository.recoverAccount().

class RecoverAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    // Solicita recuperação de senha para o email informado
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.recoverAccount(email)
    }
}