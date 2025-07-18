package com.luizeduardobrandao.tasksfirebasehilt.di

import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseAuthDataSource
import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseAuthDataSourceImpl
import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseTaskDataSource
import com.luizeduardobrandao.tasksfirebasehilt.data.datasource.FirebaseTaskDataSourceImpl
import com.luizeduardobrandao.tasksfirebasehilt.data.repository.AuthRepositoryImpl
import com.luizeduardobrandao.tasksfirebasehilt.data.repository.TaskRepositoryImpl
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// * Associa interfaces às implementações concretas
// * para DataSources e Repositories via @Binds.

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceBindsModule {

    // Quando alguém pedir FirebaseAuthDataSource, entregar FirebaseAuthDataSourceImpl
    @Binds @Singleton
    abstract fun bindAuthDataSource(
        impl: FirebaseAuthDataSourceImpl
    ) : FirebaseAuthDataSource

    @Binds @Singleton
    abstract fun bindTaskDataSource(
        impl: FirebaseTaskDataSourceImpl
    ): FirebaseTaskDataSource

    @Binds @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository
}

// Objetivo: dizer ao Hilt quando uma interface (por exemplo, AuthRepository) for
//           requisitada, qual implementação concreta (AuthRepositoryImpl) deve ser injetada.
//
// Por que? Mantém sua UI e domínio dependentes somente das interfaces, não das classes concretas.

// Por que existe?
//
// @Binds é usado quando você já tem a implementação concreta anotada com @Inject constructor e
// quer apenas dizer “interface → implementação”.
//
// Garante que em t0do lugar você injete interfaces, não classes, promovendo desacoplamento.