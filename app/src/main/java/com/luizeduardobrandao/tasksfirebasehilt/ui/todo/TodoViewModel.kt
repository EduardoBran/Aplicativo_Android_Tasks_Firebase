package com.luizeduardobrandao.tasksfirebasehilt.ui.todo

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

// * ViewModel da aba T0DO.
// * Carrega todas as tarefas e filtra apenas as com status T0DO.
@HiltViewModel
class TodoViewModel @Inject constructor(
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

    // Dispara o carregamento das tarefas, filtra por T0DO e atualiza os StateFlows.
    fun loadTasks() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val all = getTasksUseCase()                                     // busca todas
                _tasks.value = all.filter { it.status == Status.TODO }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao carregar tarefas"
            } finally {
                _loading.value = false
            }
        }
    }

    //  Avança o status da tarefa de T0DO → DOING. Em caso de sucesso, recarrega a lista.
    fun advanceTask(task: Task) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val updated = task.copy(status = Status.DOING)
                updateTaskUseCase(updated)
                // Após atualizar, recarrega a lista
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao atualizar tarefa"
            } finally {
                _loading.value = false
            }
        }
    }

    // Remove uma tarefa pelo seu ID. Em caso de sucesso, recarrega a lista.
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                deleteTaskUseCase(taskId)
                // Após deletar, recarrega a lista
                loadTasks()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Erro ao remover tarefa"
            } finally {
                _loading.value = false
            }
        }
    }
}