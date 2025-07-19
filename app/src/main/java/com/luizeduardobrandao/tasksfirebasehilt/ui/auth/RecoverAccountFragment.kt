package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentRecoverAccountBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.initToolbar
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding: FragmentRecoverAccountBinding get() = _binding!!

    private val viewModel: RecoverAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura toolbar com botão de voltar
        initToolbar(binding.toolbarRecover)
        binding.toolbarRecover.setNavigationOnClickListener {
            viewModel.resetState()            // ← limpa error
            findNavController().navigateUp()  // ← volta pra tela anterior
        }

        setupListeners()   // Configura os cliques dos botões
        setupObservers()   // Observa o fluxo de estado do ViewModel
    }

    // Configura os listeners de clique:
    //    - btnRecover: lê o e‑mail informado e chama viewModel.recover()
    private fun setupListeners() {
        binding.btnRecover.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            viewModel.recover(email)
        }
    }

    // - Observa o fluxo authState do ViewModel usando repeatOnLifecycle, coletando apenas
    //   enquanto o Fragment estiver STARTED.
    // Atualiza a UI conforme o estado: Loading, Success ou Error.
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Loading -> showLoading()
                        is AuthState.Success -> handleSuccess()
                        is AuthState.Error   -> showError(getString(state.resId))
                        is AuthState.Idle    -> hideLoading()
                    }
                }
            }
        }
    }

    // Exibe o ProgressBar e desabilita o botão de login.
    private fun showLoading() {
        binding.progressBarRecover.visibility = View.VISIBLE
        binding.btnRecover.isEnabled = false
    }

    // Oculta o ProgressBar e reabilita o botão de login.
    private fun hideLoading() {
        binding.progressBarRecover.visibility = View.INVISIBLE
        binding.btnRecover.isEnabled = true
    }

    // Trata o sucesso no envio de e‑mail:
    //    - Oculta o loading
    //     - Exibe BottomSheet informando envio
    //     - Ao clicar, navega de volta para tela de login
    private fun handleSuccess() {
        hideLoading()
        showBottomSheet(
            message = getString(R.string.text_email_sent),
            onClick = {
                findNavController().navigate(
                    RecoverAccountFragmentDirections.actionRecoverAccountFragmentToLoginFragment()
                )
            }
        )
    }

    // Exibe BottomSheet com a mensagem de erro fornecida, e reabilita a UI para nova tentativa.
    private fun showError(message: String) {
        hideLoading()
        binding.editTextEmail.text?.clear()
        showBottomSheet(
            message = message,
            onClick = { viewModel.resetState() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpa o binding para evitar vazamento de memória
        _binding = null
    }
}