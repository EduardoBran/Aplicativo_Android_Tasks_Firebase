package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.RegisterUseCase
import com.luizeduardobrandao.tasksfirebasehilt.helper.FirebaseHelper
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
                // pega a mensagem raw do Firebase
                val raw = result.exceptionOrNull()?.message ?: ""

                // traduz para o seu string resource
                val errorRes = FirebaseHelper.validError(raw)
                AuthState.Error(errorRes)
            }
        }
    }

    // Resetar Estado
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}