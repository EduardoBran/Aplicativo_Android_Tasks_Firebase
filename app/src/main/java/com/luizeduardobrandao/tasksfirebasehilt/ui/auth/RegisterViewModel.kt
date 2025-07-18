package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel responsável pelo fluxo de criação de conta.
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel(){

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    // Tentar registar um usuário com e-mail e senha (Emite Loading → Success ou Error)
    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = registerUseCase(email, password)

            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                AuthState.Error(
                    result.exceptionOrNull()?.localizedMessage ?: "Erro ao criar conta."
                )
            }
        }
    }
}