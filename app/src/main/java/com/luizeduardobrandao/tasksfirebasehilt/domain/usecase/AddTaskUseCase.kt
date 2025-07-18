package com.luizeduardobrandao.tasksfirebasehilt.domain.usecase

import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.TaskRepository
import javax.inject.Inject

// * Caso de uso para adicionar uma nova tarefa.
// * Recebe uma Task (sem id), delega ao repositório e retorna o id gerado.

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
){

    // * Executa a criação de tarefa.
    // * @param task Objeto Task contendo descrição e status; sem id definido.
    // * @return ID gerado pelo Firebase (String) em caso de sucesso, ou lança exceção em falha.

    suspend operator fun invoke(task: Task): String {

        // Chama o repositório para persistir a Task no Firebase
        return taskRepository.addTask(task)
    }
}

/*

- Os Use Cases (às vezes chamados de Interactors) são classes da camada de domínio cujo papel é
   encapsular cada operação de negócio do seu aplicativo como uma unidade coesa. Em vez de você
   espalhar lógica de orquestração direto nos ViewModels ou Activities, você:

1. Define um Use Case para cada caso de uso do seu app
   (adicionar tarefa, carregar lista, fazer login etc.).

2. Injeta nele apenas o que ele precisa (normalmente um repositório).

3. Expõe uma única função (por convensão invoke()) que executa aquela operação.


*** Por que usar Use Cases?

- Separa responsabilidades: o ViewModel só “chama” o Use Case e cuida de UI/State.
                            O Use Case cuida da lógica de negócios.

- Facilita testes: cada Use Case implementa apenas um fluxo e pode ser testado isoladamente,
                   mockando o repositório.

- Reuso: múltiplos ViewModels ou telas podem compartilhar o mesmo Use Case.

- Manutenção: adicionar validações e regras de erro em um só lugar, sem duplicação.

*/