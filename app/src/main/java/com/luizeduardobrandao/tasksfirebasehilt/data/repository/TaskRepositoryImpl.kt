package com.luizeduardobrandao.tasksfirebasehilt.data.repository

import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseTaskDataSource
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject


// * Implementação de TaskRepository que delega todas as operações de CRUD de tarefas
// ao FirebaseTaskDataSource.
// * Mantém a lógica de acesso a dados separada da camada de apresentação/UI.

class TaskRepositoryImpl @Inject constructor(
    private val taskDs: FirebaseTaskDataSource     // injetado pelo Hilt via DataSourceBindsModule
) : TaskRepository {

    // - Busca todas as tarefas do usuário. Propaga o resultado do DataSource sem alterações.
    override suspend fun getTasks(): List<Task> = taskDs.fetchAllTasks()

    // - Recupera uma única tarefa pelo seu ID.
    // Retorna null caso o DataSource não encontre o item.
    override suspend fun getTaskById(id: String): Task? = taskDs.fetchTaskById(id)

    // - Adiciona uma nova tarefa.
    // Recebe o objeto Task (sem ID) e devolve o ID gerado pelo Firebase.
    override suspend fun addTask(task: Task): String = taskDs.addTask(task)

    // - Atualiza os dados de uma tarefa existente.
    override suspend fun updateTask(task: Task) = taskDs.updateTask(task)

    // - Remove uma tarefa pelo seu ID.
    override suspend fun deleteTask(id: String) = taskDs.deleteTask(id)
}