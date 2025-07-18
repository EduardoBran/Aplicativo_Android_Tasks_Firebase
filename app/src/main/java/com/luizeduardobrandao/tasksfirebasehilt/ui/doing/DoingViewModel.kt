package com.luizeduardobrandao.tasksfirebasehilt.ui.doing

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


// * ViewModel da aba DOING.
// * Carrega todas as tarefas e filtra apenas as com status DOING.
@HiltViewModel
class DoingViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    // Lista filtrada de tarefas T0DO
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // Indica se está carregando
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Mensagem de erro, se ocorrer
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    // Carre e filtra apenas DOING
    fun loadTasks() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val all = getTasksUseCase()
                _tasks.value = all.filter { it.status == Status.DOING }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao carregar tarefas"
            } finally {
                _loading.value = false
            }
        }
    }

    // Avança o status de uma tarefa DOING → DONE e recarrega a lista.
    fun advanceTask(task: Task) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val updated = task.copy(status = Status.DONE)
                updateTaskUseCase(updated)
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao atualizar tarefa"
            } finally {
                _loading.value = false
            }
        }
    }

    // Retrocede o status de DOING → T0DO e recarrega a lista.
    fun backToTodo(task: Task) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val updated = task.copy(status = Status.TODO)
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