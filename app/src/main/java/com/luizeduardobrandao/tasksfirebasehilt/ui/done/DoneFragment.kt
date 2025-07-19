package com.luizeduardobrandao.tasksfirebasehilt.ui.done

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentDoneBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import com.luizeduardobrandao.tasksfirebasehilt.ui.adapter.TaskAdapter
import com.luizeduardobrandao.tasksfirebasehilt.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Fragment da aba DONE.
// * Exibe tarefas concluídas e permite:
// *  • Retroceder status para DOING
// *  • Editar tarefa
// *  • Excluir tarefa

@AndroidEntryPoint
class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DoneViewModel by viewModels()

    // Adapter do RecyclerView para exibir as tarefas
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        setupAdapter()          // Prepara o adapter e define callbacks de ação
        setupObservers()        // Inicia a coleta dos StateFlows do ViewModel
        viewModel.loadTasks()   // Carrega as tarefas com status DONE
    }

    // Inicializa o TaskAdapter atribuindo ao RecyclerView, e define o que fazer em cada
    // ação do item:
    //    - SELECT_BACK: retorna tarefa para DOING
    //    - SELECT_EDIT: navega para tela de edição
    //    - SELECT_REMOVE: confirma e exclui tarefa
    private fun setupAdapter() {
        adapter = TaskAdapter { task, action ->
            when (action) {
                TaskAdapter.SELECT_BACK   -> {
                    viewModel.backToDoing(task)
                    Toast.makeText(
                        requireContext(), R.string.text_form_task_update, Toast.LENGTH_SHORT
                    ).show()
                }
                TaskAdapter.SELECT_EDIT   -> navigateToForm(task)
                TaskAdapter.SELECT_REMOVE -> confirmDelete(task)
                TaskAdapter.SELECT_DETAILS -> {
                    // navega usando o action definid o em main_graph
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(task)
                    )
                }
            }
        }
        binding.rvTasks.adapter = adapter
    }

    // Observa os StateFlows do ViewModel dentro de repeatOnLifecycle,  para coleta apenas
    // quando o Fragment estiver STARTED:
    //    - loading: exibe/oculta ProgressBar
    //    - tasks: atualiza lista e mostra placeholder se vazia
    //    - error: exibe BottomSheet com mensagem
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // loading → mostra/oculta ProgressBar
                launch {
                    viewModel.loading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }
                // tasks → popula lista, placeholder e texto de “vazio”
                launch {
                    viewModel.tasks.collect { list ->
                        adapter.submitList(list)
                        binding.rvTasks.isVisible = list.isNotEmpty()

                        if (list.isEmpty()) {
                            // Exibe o TextView de vazio com a nova mensagem
                            binding.textInfo.apply {
                                isVisible = true
                                text = getString(R.string.text_list_task_empty)
                            }
                            binding.progressBar.isVisible = false
                        } else {
                            binding.textInfo.isVisible = false
                        }
                    }
                }
                // error → exibe BottomSheet de erro
                launch {
                    viewModel.error.collect { msg ->
                        msg?.let { showBottomSheet(message = it) }
                    }
                }
            }
        }
    }

    // Navega para FormTaskFragment em modo edição, passando a Task selecionada.
    private fun navigateToForm(task: Task) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(task)
        )
    }

    // Exibe BottomSheet de confirmação e, se confirmado, chama o ViewModel para excluir a tarefa.
    private fun confirmDelete(task: Task) {
        showBottomSheet(
            titleDialog = R.string.text_title_dialog_confirm_remove,
            message = getString(R.string.text_message_dialog_confirm_remove),
            titleButton = R.string.text_button_dialog_confirm_logout,
            onClick = {
                viewModel.deleteTask(task.id)
                Toast.makeText(
                    requireContext(), R.string.text_remove, Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}