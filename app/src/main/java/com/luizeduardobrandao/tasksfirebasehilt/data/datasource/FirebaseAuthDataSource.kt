package com.luizeduardobrandao.tasksfirebasehilt.data.datasource

// * Contrato de baixo‐nível para operações de autenticação no Firebase.
// * Esta interface abstrai detalhes do FirebaseAuth e será injetada nos repositórios
// * para uso na camada de domínio/UI.

interface FirebaseAuthDataSource {

    // - Retorna o ID do usuário atualmente autenticado (ou null se ninguém estiver logado).
    // Operação rápida, não faz I/O (entrada e saída de dados) bloqueante.
    fun currentUserId(): String?

    // - Tenta fazer login com email e senha.
    // É uma operação assíncrona que pode levar tempo (network I/O), por isso é marcada
    // como suspend para uso em coroutines.
    suspend fun login(email: String, password: String): Result<Unit>

    // - Cria uma nova conta no Firebase com email e senha.
    // Também suspende a coroutine até a resposta do Firebase.
    suspend fun register(email: String, password: String): Result<Unit>

    // - Envia um e‑mail de recuperação de senha para o usuário.
    // Suspende até que o Firebase retorne confirmação ou erro.
    suspend fun recoverAccount(email: String): Result<Unit>

    // - Desloga o usuário atual. Chamada síncrona rápida que limpa o estado interno do FirebaseAuth.
    fun logout()
}

// * Contrato: lista os métodos de autenticação que precisamos.