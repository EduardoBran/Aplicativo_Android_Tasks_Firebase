package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel responsável pela lógica de login.
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // Estado interno mutável exposto como Flow imutável
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    // Inicia o fluxo de Login (Atualiza _authState para Loading, Success ou Error)
    fun login(email: String, password: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // Executa o use case; recebe Result<Unit>
            val result = loginUseCase(email, password)

            _authState.value = if(result.isSuccess) {
                AuthState.Success
            } else {
                // Pega a mensagem da exceção para exibir na UI
                AuthState.Error(
                    result.exceptionOrNull()?.localizedMessage ?: "Erro desconhecido.")
            }
        }
    }

    // Resetar Estado
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}