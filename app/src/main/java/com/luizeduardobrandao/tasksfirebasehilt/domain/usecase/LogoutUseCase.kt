package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import javax.inject.Inject

// * Caso de uso para deslogar o usuário atual.
// * Encapsula a chamada a AuthRepository.logout().

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    // Executa o logout imediatamente. Não retorna valor; dispara a ação de deslogar.
    operator fun invoke() {
        authRepository.logout()
    }
}