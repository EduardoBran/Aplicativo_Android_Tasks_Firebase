package com.luizeduardobrandao.tasksfirebasehilt.ui.task

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
import androidx.navigation.fragment.navArgs
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.data.model.Task
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentFormTaskBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.Status
import com.luizeduardobrandao.tasksfirebasehilt.helper.initToolbar
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// * Fragment para criação ou edição de uma Task.
// * Se args.task != null, entra em modo “edição”; caso contrário, modo “nova tarefa”.
@AndroidEntryPoint
class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FormTaskViewModel by viewModels()

    // SafeArgs para receber a Task (ou null)
    private val args: FormTaskFragmentArgs by navArgs()

    // Referência à task existente, se estivermos editando
    private var existingTask: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbarForm)           // Configura toolbar com back
        loadArgs()                                 // Preenche campos se for edição
        setupSaveButtonListener()                  // Configura clique no botão Salvar
        setupObservers()                           // Observa estados de salvamento
    }

    // Lê os args e, se houver Task, ajusta título e pré-preenche campos.
    private fun loadArgs() {
        existingTask = args.task

        if (existingTask != null) {
            binding.textTitleForm.text = getString(R.string.text_title_toolbar_edit_task)
            binding.editTextDescription.setText(existingTask!!.description)
            when (existingTask!!.status) {
                Status.TODO  -> binding.rgStatus.check(R.id.rbTodo)
                Status.DOING -> binding.rgStatus.check(R.id.rbDoing)
                Status.DONE  -> binding.rgStatus.check(R.id.rbDone)
            }
        } else {
            binding.textTitleForm.text = getString(R.string.text_title_toolbar_new_task)
        }
    }

    // Configura o listener do botão Salvar:
    //    - Valida descrição não vazia
    //    - Lê o status selecionado
    //    - Cria ou atualiza a Task, passando ao ViewModel
    private fun setupSaveButtonListener() {
        binding.btnSave.setOnClickListener {
            val desc = binding.editTextDescription.text.toString().trim()
            if (desc.isEmpty()){
                showBottomSheet(message = getString(R.string.text_form_task_error))
                return@setOnClickListener
            }

            val status = when (binding.rgStatus.checkedRadioButtonId) {
                R.id.rbDoing -> Status.DOING
                R.id.rbDone  -> Status.DONE
                else          -> Status.TODO
            }

            val task = existingTask
                ?.copy(description = desc, status = status)
                ?: Task(description = desc, status = status)
            viewModel.saveTask(task)
        }
    }

    // Observa os fluxos do ViewModel dentro de repeatOnLifecycle:
    //    - isSaving: exibe/oculta progress e habilita/desabilita botão
    //    - saveSuccess: navega de volta se salvar for bem‑sucedido
    //    - error: mostra BottomSheet em caso de falha
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Indicador de salvamento em progresso
                launch {
                    viewModel.isSaving.collect { saving ->
                        binding.progressBarForm.isVisible = saving
                        binding.btnSave.isEnabled = !saving
                    }
                }

                // Navega de volta quando o save for bem‑sucedido
                launch {
                    viewModel.saveSuccess.collect { success ->
                        if (success) {
                            findNavController().navigate(
                                FormTaskFragmentDirections.actionFormTaskFragmentToHomeFragment()
                            )
                            Toast.makeText(
                                requireContext(), R.string.text_form_task_saved, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                // Exibe erro em BottomSheet se houver falha
                launch {
                    viewModel.error.collect { msg ->
                        msg?.let { showBottomSheet(message = it) }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}