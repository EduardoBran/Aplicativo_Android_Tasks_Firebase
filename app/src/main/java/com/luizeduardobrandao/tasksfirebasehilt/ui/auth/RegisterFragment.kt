package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentRegisterBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.initToolbar
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura a toolbar com botão de voltar
        initToolbar(binding.toolbarRegister)
        binding.toolbarRegister.setNavigationOnClickListener {
            viewModel.resetState()            // ← limpa error
            findNavController().navigateUp()  // ← volta pra tela anterior
        }

        setupListeners()   // Configura os cliques dos botões
        setupObservers()   // Observa o estado de autenticação
    }

    // Configura os listeners de clique:
    //    - btnRegister: lê email e senha e chama viewModel.register()
    private fun setupListeners(){

        binding.btnRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            viewModel.register(email, password)
        }
    }

    // - Observa o fluxo authState do ViewModel usando repeatOnLifecycle, garantindo coleta
    //   apenas enquanto o Fragment estiver STARTED.
    // Em cada estado, chama o mét0do que atualiza a UI.

    private fun setupObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect() { state ->
                    when (state) {
                        is AuthState.Loading -> showLoading()
                        is AuthState.Success -> navigateToHome()
                        is AuthState.Error -> showError(state.message)
                        is AuthState.Idle -> hideLoading()
                    }
                }
            }
        }
    }

    // Exibe o ProgressBar e desabilita o botão de login.
    private fun showLoading() {
        binding.progressBarRegister.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false
    }

    // Oculta o ProgressBar e reabilita o botão de login.
    private fun hideLoading() {
        binding.progressBarRegister.visibility = View.INVISIBLE
        binding.btnRegister.isEnabled = true
    }

    // Navega para a HomeFragment após registro bem‑sucedido.
    private fun navigateToHome() {
        hideLoading()
        findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        )
        Toast.makeText(
            requireContext(),
            R.string.text_login_validated,
            Toast.LENGTH_SHORT
        ).show()
    }

    // Exibe BottomSheet com a mensagem de erro fornecida, e reabilita a UI para nova tentativa.
    private fun showError(message: String) {
        hideLoading()
        binding.editTextEmail.text?.clear()
        binding.editTextPassword.text?.clear()
        showBottomSheet(
            message = message,
            onClick = { viewModel.resetState() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Evita vazamento de memória liberando o binding
        _binding = null
    }
}