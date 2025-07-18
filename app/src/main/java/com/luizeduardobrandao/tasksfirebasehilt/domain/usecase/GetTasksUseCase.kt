package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject

// * Caso de uso para obter todas as tarefas do usuário.
// * Retorna lista completa, sem filtrar status.

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    // Executa a leitura de todas as tarefas
    suspend operator fun invoke(): List<Task> {
        return taskRepository.getTasks()
    }
}