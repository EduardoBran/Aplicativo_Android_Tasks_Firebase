package com.luizeduardobrandao.tasksfirebasehilt.data.datasource

import com.google.firebase.database.DatabaseReference
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// * Implementação de FirebaseTaskDataSource que usa DatabaseReference para executar operações
// no Firebase Realtime Database.
// * Cada chamada faz await() na Task do Firebase para suspender a coroutine até a
// operação ser concluída.

class FirebaseTaskDataSourceImpl @Inject constructor(
    private val dbRef: DatabaseReference                // injetado pelo Hilt via DataSourceFirebaseModule
) : FirebaseTaskDataSource{

    // - Realiza um get() no nó principal de tarefas e constrói uma lista a partir de
    // cada child snapshot convertido em Task.
    override suspend fun fetchAllTasks(): List<Task> {

        val snapshot = dbRef.get().await() // Pausa até receber os dados
        val list = mutableListOf<Task>()

        // Itera sobre cada filho e converte para Task
        snapshot.children.forEach { child ->
            child.getValue(Task::class.java)?.let { list.add(it) }
        }

        return list
    }

    // - Acessa diretamente o nó da tarefa por ID e retorna o objeto Task, ou null se
    // o nó não existir.
    override suspend fun fetchTaskById(id: String): Task? {

        val snapshot = dbRef.child(id).get().await()
        return snapshot.getValue(Task::class.java)
    }

    // - Gera uma nova chave dentro do nó de tarefas, define task.id, salva o objeto
    // no Firebase e retorna a chave.
    // - Lança IllegalStateException se não conseguir gerar a chave.
    override suspend fun addTask(task: Task): String {

        val key = dbRef.push().key
            ?: throw IllegalStateException("Não foi possível gerar chave")

        task.id = key
        dbRef.child(key).setValue(task).await()

        return key
    }

    // - Substitui o nó existente correspondente a task.id pelos dados atuais do objeto Task.
    override suspend fun updateTask(task: Task) {
        dbRef.child(task.id).setValue(task).await()
    }

    // - Remove o nó da tarefa identificado por ID.
    override suspend fun deleteTask(id: String) {
        dbRef.child(id).removeValue().await()
    }
}