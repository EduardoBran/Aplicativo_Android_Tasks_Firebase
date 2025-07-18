package com.luizeduardobrandao.tasksfirebasehilt.ui.home

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
import com.google.android.material.tabs.TabLayoutMediator
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentHomeBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.showBottomSheet
import com.luizeduardobrandao.tasksfirebasehilt.ui.doing.DoingFragment
import com.luizeduardobrandao.tasksfirebasehilt.ui.done.DoneFragment
import com.luizeduardobrandao.tasksfirebasehilt.ui.todo.TodoFragment
import com.luizeduardobrandao.tasksfirebasehilt.ui.adapter.ViewPageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// * Fragment responsável pela tela principal com abas T0DO / DOING / DONE e botão de logout.
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayout()    // Configura as abas e o pager
        setupListeners()   // Configura o clique no botão de logout
        setupObservers()   // Observa o evento de logout no ViewModel
    }

    // - Configura o ViewPager2 com suas três abas (T0do, Doing, Done) e vincula
    //   ao TabLayout via TabLayoutMediator.
    private fun initTabLayout() {
        val pageAdapter = ViewPageAdapter(requireActivity())

        // Alterar o componente dinamicamente
        binding.viewPager.adapter = pageAdapter

        // Adiciona os títulos
        pageAdapter.addFragment(TodoFragment(), R.string.status_task_todo)
        pageAdapter.addFragment(DoingFragment(), R.string.status_task_doing)
        pageAdapter.addFragment(DoneFragment(), R.string.status_task_done)

        // Adiciona o limite de quantos fragments tem no TabLayout
        binding.viewPager.offscreenPageLimit = pageAdapter.itemCount

        // Associa o ViewPager2 ao TabLayout
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            // Setar o título
            tab.text = getString(pageAdapter.getTitle(position))
        }.attach()
    }

    // - Define o listener de clique para o botão de logout, que dispara o use case no ViewModel.
    private fun setupListeners(){
        binding.btnLogout.setOnClickListener {
            // Exibe confirmação antes de deslogar
            showBottomSheet(
                titleDialog   = R.string.text_title_dialog_confirm_logout,
                titleButton   = R.string.text_button_dialog_confirm_logout,
                message       = getString(R.string.text_message_dialog_confirm_logout),
                onClick       = {
                    // Só aqui, quando confirma, dispara logout
                    viewModel.logout()
                }
            )
        }
    }

    // - Observa o fluxo logoutEvent do ViewModel usando repeatOnLifecycle, para reagir apenas
    //   quando o Fragment estiver STARTED.
    // Ao receber o evento, navega de volta para a tela de login e limpa a pilha conforme main_graph
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutEvent.collect {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    )
                    Toast.makeText(
                        requireContext(), R.string.text_toast_confirm_logout, Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpa o binding para evitar vazamento de memória
        _binding = null
    }
}