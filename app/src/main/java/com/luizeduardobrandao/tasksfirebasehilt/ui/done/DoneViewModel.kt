package com.luizeduardobrandao.tasksfirebasehilt.ui.done

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.DeleteTaskUseCase
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.GetTasksUseCase
import com.luizeduardobrandao.tasksfirebasehilt.domain.usecase.UpdateTaskUseCase
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
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    // Carrega e filtra apenas DONE.
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

    // Retrocede o status de DONE → DOING e recarrega a lista.
    fun backToDoing(task: Task) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val updated = task.copy(status = Status.DOING)
                updateTaskUseCase(updated)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao atualizar tarefa"
            } finally {
                _loading.value = false
            }
        }
    }

    // Remove a tarefa com o ID informado e recarrega a lista.
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                deleteTaskUseCase(taskId)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao remover tarefa"
            } finally {
                _loading.value = false
            }
        }
    }
}