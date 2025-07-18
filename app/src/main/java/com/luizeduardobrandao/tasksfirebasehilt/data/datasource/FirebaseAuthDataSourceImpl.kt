package com.luizeduardobrandao.tasksfirebasehilt.data.datasource

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// * Implementação concreta de FirebaseAuthDataSource usando o SDK FirebaseAuth.
// * Cada mét0do que faz chamadas de rede usa await() para suspender a coroutine até a
// * operação completar, sem bloquear a thread.

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth                    // injetado pelo Hilt via DataSourceFirebaseModule
) : FirebaseAuthDataSource {

    // - Retorna o UID do usuário logado, ou null se não houver sessão ativa.
    override fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    // - Chama signInWithEmailAndPassword(email, password) do FirebaseAuth, aguarda o
    // resultado com await() e encapsula em Result.
    override suspend fun login(email: String, password: String): Result<Unit> =
        try {
            // Pausa a coroutine até o Firebase completar o login
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            // Em caso de erro (credenciais inválidas, sem conexão etc.)
            Result.failure(e)
        }

    // - Cria um novo usuário no FirebaseAuth.
    override suspend fun register(email: String, password: String): Result<Unit> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    // - Solicita o envio de e-mail de reset de senha.
    override suspend fun recoverAccount(email: String): Result<Unit> =
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    // - Desloga o usuário atual chamando signOut() do FirebaseAuth.
    override fun logout() {
        auth.signOut()
    }
}

// Anotação @Inject constructor: informa ao Hilt como instanciar essa classe, injetando
// FirebaseAuth (fornecido pelo DataSourceFirebaseModule).