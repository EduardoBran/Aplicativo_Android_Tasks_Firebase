package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.RecoverAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel responsável pelo envio de email de recuperação.
@HiltViewModel
class RecoverAccountViewModel @Inject constructor(
    private val recoverAccountUseCase: RecoverAccountUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Envia email de recuperação para o usuário informado. (Emite Loading → Success ou Error)
    fun recover(email: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = recoverAccountUseCase(email)

            _authState.value = if (result.isSuccess) {
                AuthState.Success
            } else {
                AuthState.Error(
                    result.exceptionOrNull()?.localizedMessage ?: "Erro ao recuperar conta")
            }
        }
    }

    // Resetar Estado
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}