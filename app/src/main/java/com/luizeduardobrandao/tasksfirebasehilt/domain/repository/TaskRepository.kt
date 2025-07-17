package com.luizeduardobrandao.tasksfirebasehilt.domain.repository

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task

// * Define as operações CRUD para o modelo Task.
// * Permite à camada de domínio trabalhar independentemente do Firebase.

interface TaskRepository {

    // Retorna todas as tarefas do usuário.
    suspend fun getTasks(): List<Task>

    // Retorna uma tarefa específica pelo ID.
    suspend fun getTaskById(id: String): Task?

    // Adiciona nova tarefam retorna o ID gerado.
    suspend fun addTask(task: Task): String

    // Atualiza uma tarefa existente.
    suspend fun updateTask(task: Task)

    // Remove uma tarefa pelo ID.
    suspend fun deleteTask(id: String)
}