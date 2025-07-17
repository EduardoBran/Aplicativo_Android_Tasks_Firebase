package com.luizeduardobrandao.tasksfirebasehilt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.tasksfirebasehilt.helper.Status
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.databinding.ItemTaskBinding

// * Adapter para exibir uma lista de Task em um RecyclerView.
// * @param taskList lista de objetos Task a serem renderizados (não é necessário com o DiffUtil)
// * Utilizando DiffUtil para melhor performance (herda de ListAdapter)

class TaskAdapter(
    private val taskSelected: (Task, Int) -> Unit
): ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    // ViewHolder interno que mantém referência ao binding gerado pelo layout item_task.xml
    inner class MyViewHolder(
        val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root)


    // FLAGS para os 5 eventos de cliques e callback para o DiffUtil
    companion object {
        const val SELECT_BACK: Int = 1
        const val SELECT_REMOVE: Int = 2
        const val SELECT_EDIT: Int = 3
        const val SELECT_DETAILS: Int = 4
        const val SELECT_NEXT: Int = 5

        // Implementação do DiffUtil
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {

                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description
            }
        }
    }


    // Infla o layout do item e cria um ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    // Retorna a quantidade de itens na lista (não precisa desta implementação com o DiffUtil)
    // override fun getItemCount() = taskList.size

    // Vincula os dados da Task à view correspondente no ViewHolder.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Obtém a Task na posição atual com a configuração do DiffUtil
        val task = getItem(position)

        // Define o texto da descrição
        holder.binding.textDescription.text = task.description

        // Exibição e monitoramento de cliques dos indicadores e botões
        setIndicators(task, holder)

        // Cliques botão Remover, Editar e Detalhes
        holder.binding.btnDelete.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }
    }

    // Função para exibição dos indicadores (seta de voltar e avançar)
    private fun setIndicators(task: Task, holder: MyViewHolder) {
        when (task.status){
            Status.TODO -> {
                holder.binding.btnNext.isVisible = true

                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }

            Status.DOING -> {
                holder.binding.btnBack.isVisible = true
                holder.binding.btnNext.isVisible = true

                holder.binding.btnNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }

            Status.DONE -> {
                holder.binding.btnBack.isVisible = true

                holder.binding.btnBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }
        }
    }
}