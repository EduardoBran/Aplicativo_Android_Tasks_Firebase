package com.luizeduardobrandao.tasksfirebasehilt.data.datasource

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task

// * Contrato de operações CRUD para tarefas usando o Realtime Database do Firebase.
// * Cada mét0do é suspenso para uso em coroutines, pois envolve I/O de rede.

interface FirebaseTaskDataSource {

    // - Busca todas as tarefas do nó “tasks/uid” no Firebase.
    suspend fun fetchAllTasks(): List<Task>

    // -  Busca uma única tarefa pelo seu ID ou null se não existir.
    suspend fun fetchTaskById(id: String): Task?

    // - Adiciona uma nova tarefa ao banco.
    suspend fun addTask(task: Task): String

    // - Atualiza os dados de uma tarefa existente.
    suspend fun updateTask(task: Task)

    // - Remove uma tarefa do Firebase pelo seu ID.
    suspend fun deleteTask(id: String)
}