package com.luizeduardobrandao.tasksfirebasehilt.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel da tela Home (aquela com as abas T0DO/DOING/DONE).
// * Responsável por disparar o logout e notificar a UI para navegar de volta ao Login.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    // Evento único para notificar a UI de que o logout foi concluído
    private val _logoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val logoutEvent: SharedFlow<Unit> = _logoutEvent


    // Executa o logout e emite um evento para a UI.
    fun logout(){
        viewModelScope.launch {
            logoutUseCase()             // Limpa credenciais do Firebase
            _logoutEvent.emit(Unit)     // Notifica o Fragment para navegar ao Login
        }
    }
}