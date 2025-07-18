package com.luizeduardobrandao.tasksfirebasehilt.data.repository

import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseAuthDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert.assertTrue
import org.mockito.kotlin.verify

class AuthRepositoryImplTest {

    private val authDs = mock<FirebaseAuthDataSource>()

    private lateinit var repo: AuthRepositoryImpl

    @Before
    fun setup() {
        repo = AuthRepositoryImpl(authDs)
    }

    @Test
    fun loginDelegaRetornaSucesso(): Unit = runBlocking {
        // 1. Configura o mock para retornar sucesso
        whenever(authDs.login("u@e.com", "pass")).thenReturn(Result.success(Unit))

        // 2. Chama a função suspend dentro do runBlocking
        val result = repo.login("u@e.com", "pass")

        // 3. Verifica o resultado e a interação com o DataSource
        assertTrue(result.isSuccess)
        verify(authDs).login("u@e.com", "pass")
    }
}