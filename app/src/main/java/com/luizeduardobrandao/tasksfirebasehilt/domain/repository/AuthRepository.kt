package com.luizeduardobrandao.tasksfirebasehilt.domain.repository

// * Define as operações de autenticação disponíveis na aplicação.
// * Desacopla a camada de domínio das implementações concretas do Firebase.

interface AuthRepository {

    // Faz login com email/senha
    suspend fun login (email: String, password: String): Result<Unit>

    // Registrar um novo usuário
    suspend fun register (email: String, password: String): Result<Unit>

    // Envia email de recuperação de senha
    suspend fun recoverAccount(email: String): Result<Unit>

    // Desloga o usuário atual
    fun logout()

    // Verifica se já existe um usuário autenticado
    fun isAuthenticated(): Boolean
}

// Fluxo

// 1. AuthRepository
//  – É a interface que sua camada de apresentação (ViewModel, Activity) vai usar.

// 2. AuthRepositoryImpl
//  – Implementa aquele contrato e depende de FirebaseAuthDataSource para fazer o trabalho
//    de autenticação.

// 3. FirebaseAuthDataSource
//  – É a interface de baixo nível que define métodos como login, register, etc.

// 4. FirebaseAuthDataSourceImpl
//  – É a implementação concreta dessa interface, que chama diretamente o SDK do
//    Firebase (FirebaseAuth) usando await().

// 5. DataSourceFirebaseModule
//  – Um módulo @Module @InstallIn(SingletonComponent::class) que fornece instâncias de
//    FirebaseAuth e DatabaseReference via métodos anotados com @Provides.
//  – Não fornece diretamente FirebaseAuthDataSourceImpl, mas sim os objetos que ele
//    precisa (o FirebaseAuth e o DatabaseReference).

// 6. DataSourceBindsModule
//  – Um módulo @Module @InstallIn(SingletonComponent::class) abstrato onde você usa @Binds
//    para dizer ao Hilt:

//  - “Quando alguém pedir FirebaseAuthDataSource, entregue-me uma instância de FirebaseAuthDataSourceImpl.”
//  - “Quando alguém pedir AuthRepository, entregue-me uma instância de AuthRepositoryImpl.”
//  – É aqui que as interfaces são “coladas” às classes concretas.

/*

Em tempo de compilação, o Hilt:

- Gera o grafo de componentes a partir de @HiltAndroidApp em MyApp.
- Registra os provedores de FirebaseAuth e DatabaseReference (do DataSourceFirebaseModule).
- Registra os binds de interface→implementação (do DataSourceBindsModule).
- Permite que, em qualquer classe anotada com @AndroidEntryPoint ou @HiltViewModel, você injete
  AuthRepository ou FirebaseAuthDataSource sem se preocupar em instanciar nada manualmente.

 */