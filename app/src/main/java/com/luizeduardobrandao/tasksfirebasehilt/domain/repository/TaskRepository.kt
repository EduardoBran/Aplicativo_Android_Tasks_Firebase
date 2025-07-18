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

// Fluxo

// 1. TaskRepository
//  – É a interface que sua UI/ViewModel consome.

// 2. TaskRepositoryImpl
//  – Implementa TaskRepository e depende de FirebaseTaskDataSource para fazer o CRUD de tarefas.

// 3. FirebaseTaskDataSource
//  – Interface de baixo nível que define métodos como fetchAllTasks(), addTask(), etc.

// 4. FirebaseTaskDataSourceImpl
//  – Classe concreta que implementa FirebaseTaskDataSource, usando o DatabaseReference do
//    Firebase e chamando .await() para suspender a coroutine até o resultado.

// 5. DataSourceFirebaseModule
//  – Módulo @Provides que cria e disponibiliza instâncias de FirebaseAuth e DatabaseReference
//    (os “ingredientes” que FirebaseTaskDataSourceImpl precisa).

// 6. DataSourceBindsModule
//  – Módulo @Binds que diz ao Hilt:

//   - Quando alguém pedir FirebaseTaskDataSource, entregue uma instância de
//     FirebaseTaskDataSourceImpl.
//
//   - E igualmente para TaskRepository → TaskRepositoryImpl, FirebaseAuthDataSource → FirebaseAuthDataSourceImpl, etc.

// Assim, o Hilt sabe exatamente qual implementação usar para cada interface e como criar
// cada objeto em tempo de compilação.