package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject

// * Caso de uso para buscar uma única tarefa pelo ID.
// * Retorna a Task ou null se não existir.

class GetTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    // Executa a consulta com ID da tarefa ou retorna null se não tiver tarefa
    suspend operator fun invoke(taskId: String): Task? {

        return taskRepository.getTaskById(taskId)
    }
}