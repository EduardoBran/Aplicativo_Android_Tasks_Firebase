package com.luizeduardobrandao.tasksfirebasehilt.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


// * Fornece instâncias de FirebaseAuthDataSource e FirebaseTaskDataSource via Hilt.

@Module                                      // Define este objeto como fonte de dependências
@InstallIn(SingletonComponent::class)        // Disponibiliza no escopo de toda a aplicação
object DataSourceFirebaseModule {

    @Provides                                      // Diz ao Hilt “use este mét0do para obter um FirebaseAuth”
    @Singleton                                     // Mantenha uma única instância durante t0do o app
    fun provideFirebaseAuth(): FirebaseAuth {
        // Retorna a instância singleton de autenticação do Firebase
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(auth: FirebaseAuth): DatabaseReference {
        val uid = auth.currentUser?.uid ?: ""
        return Firebase.database.reference.child("tasks").child(uid)
    }
}

// - FirebaseAuthDataSource encapsula operações de autenticação (login, registro e recuperação)
//   utilizando FirebaseAuth.
//
// - FirebaseTaskDataSource gerencia as operações CRUD de Task utilizando o DatabaseReference
//   (referência ao nó tasks/uid).

// O quê faz?
//
// @Module sinaliza que aqui há métodos que fornecem dependências.
//
// @InstallIn(SingletonComponent::class) define que essas instâncias vivem pelo ciclo de
// toda a aplicação.
//
// @Provides indica o como criar: FirebaseAuth.getInstance() e um DatabaseReference já
// apontando para /tasks/{uid}.