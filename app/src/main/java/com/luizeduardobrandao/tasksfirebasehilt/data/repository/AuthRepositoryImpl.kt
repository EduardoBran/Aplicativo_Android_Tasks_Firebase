package com.luizeduardobrandao.tasksfirebasehilt.data.repository

import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseAuthDataSource
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import javax.inject.Inject

// * Implementação de AuthRepository que delega todas as operações de autenticação
//   ao FirebaseAuthDataSource.
// * Essa classe mantém a camada de domínio (use cases/ViewModels) desacoplada dos
// * detalhes do Firebase.

class AuthRepositoryImpl @Inject constructor(
    private val authDs: FirebaseAuthDataSource  // injetado pelo Hilt via DataSourceBindsModule
) : AuthRepository {

    // - Tenta autenticar o usuário com email e senha. Simplesmente repassa a chamada ao DataSource,
    // que retorna um Result<Unit> indicando sucesso ou falha.
    override suspend fun login(email: String, password: String): Result<Unit> =
        authDs.login(email, password)

    // - Cria uma nova conta de usuário no Firebase.
    // Delegação direta ao mét0do register do DataSource.
    override suspend fun register(email: String, password: String): Result<Unit> =
        authDs.register(email, password)

    // - Envia o e‑mail de recuperação de senha. A camada de domínio não precisa saber como
    // isso é feito, apenas que o DataSource cuidará da lógica de envio.
    override suspend fun recoverAccount(email: String): Result<Unit> =
        authDs.recoverAccount(email)

    // - Desloga o usuário atual. Chama o mét0do logout síncrono do DataSource,
    // que executa auth.signOut() internamente.
    override fun logout() = authDs.logout()

    // - Verifica se há um usuário autenticado no momento.
    // Usa currentUserId() do FireBaseAuthDataSource, que retorna null se ninguém estiver logado.
    override fun isAuthenticated(): Boolean = authDs.currentUserId() != null
}