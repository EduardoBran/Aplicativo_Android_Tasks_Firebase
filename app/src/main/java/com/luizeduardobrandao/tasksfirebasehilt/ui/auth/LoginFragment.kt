package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentLoginBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resetState()

        setupListeners()  // Configura os cliques dos botões
        setupObservers()  // Configura a coleta de estados do ViewModel
    }

    // Configura os listeners de clique:
    //   - btnRegister: navega para tela de registro
    //   - btnRecover: navega para tela de recuperação de conta
    //   - btnLogin: lê email e senha e chama viewModel.login()
    private fun setupListeners() {

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRecoverAccountFragment()
            )
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    // - Observa o fluxo authState do ViewModel usando repeatOnLifecycle, para coletar somente
    //   enquanto o Fragment estiver STARTED.
    // - A cada novo estado, chama a função correspondente para atualizar a UI.
    private fun setupObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect() { state ->
                    when (state) {
                        is AuthState.Loading -> showLoading()
                        is AuthState.Success -> handleSuccess()
                        is AuthState.Error -> showError(state.message)
                        is AuthState.Idle -> hideLoading()
                    }
                }
            }
        }
    }

    // Exibe o ProgressBar e desabilita o botão de login.
    private fun showLoading() {
        binding.progressBarLogin.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
    }

    // Oculta o ProgressBar e reabilita o botão de login.
    private fun hideLoading() {
        binding.progressBarLogin.visibility = View.INVISIBLE
        binding.btnLogin.isEnabled = true
    }

    // Trata o estado de sucesso: esconde o loading e navega para a Home.
    private fun handleSuccess() {
        hideLoading()
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToHomeFragment()
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
        // Evita vazamento de memória referenciando a view após destruição
        _binding = null
    }
}