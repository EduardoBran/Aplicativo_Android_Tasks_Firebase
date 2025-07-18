package com.luizeduardobrandao.tasksfirebasehilt.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.AddTaskUseCase
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel do formulário de criação/edição de tarefa.
// * Decide se chama AddTaskUseCase ou UpdateTaskUseCase com base na existência de `task.id`.
@HiltViewModel
class FormTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    // Indica se a operação está em andamento
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    // Evento de sucesso (tarefa criada ou atualizada)
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    // Mensagem de erro, se ocorrer
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    // Salva a Task. Se `task.id` for vazio, cria; caso contrário, atualiza.
    fun saveTask(task: Task) {
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null

            try {
                if (task.id.isEmpty()) {
                    addTaskUseCase(task)                 // Cria nova tarefa
                } else {
                    updateTaskUseCase(task)              // Atualiza Existente
                }
                _saveSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao salvar tarefa"
            } finally {
                _isSaving.value = false
            }
        }
    }
}