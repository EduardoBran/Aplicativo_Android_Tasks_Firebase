package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject

// * Caso de uso para remover uma tarefa existente.
// * Dado o ID, delega ao repositório para deletar.

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    // Executa a remoção. @param taskId - ID da tarefa a ser deletada
    suspend operator fun invoke(taskId: String) {
        // Repositório faz a operação de remoção no Firebase
        taskRepository.deleteTask(taskId)
    }
}