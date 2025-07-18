package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject

// * Caso de uso para atualizar uma tarefa existente.
// * Encapsula a chamada a TaskRepository.updateTask().

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    // Atualiza no Firebase os dados da Task (id, description, status)
    // @param task - Objeto Task já com id e campos modificados
    suspend operator fun invoke(task: Task) {
        taskRepository.updateTask(task)
    }
}