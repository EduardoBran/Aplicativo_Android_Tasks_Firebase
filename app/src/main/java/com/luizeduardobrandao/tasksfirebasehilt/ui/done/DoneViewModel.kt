package com.luizeduardobrandao.tasksfirebasehilt.ui.done

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.GetTasksUseCase
import com.luizeduardobrandao.tasksfirebasehilt.helper.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel da aba DONE.
// * Carrega todas as tarefas e filtra apenas as com status DONE.
@HiltViewModel
class DoneViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Carrega e filtra apenas DONE.
     */
    fun loadTasks() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val all = getTasksUseCase()
                _tasks.value = all.filter { it.status == Status.DONE }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao carregar tarefas"
            } finally {
                _loading.value = false
            }
        }
    }
}