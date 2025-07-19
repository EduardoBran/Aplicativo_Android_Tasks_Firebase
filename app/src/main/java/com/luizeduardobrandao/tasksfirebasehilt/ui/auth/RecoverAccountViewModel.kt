package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.RecoverAccountUseCase
import com.luizeduardobrandao.tasksfirebasehilt.helper.FirebaseHelper
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