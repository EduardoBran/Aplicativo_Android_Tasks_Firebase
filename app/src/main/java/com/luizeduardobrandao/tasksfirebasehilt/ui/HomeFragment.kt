package com.luizeduardobrandao.tasksfirebasehilt.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentHomeBinding
import com.luizeduardobrandao.tasksfirebasehilt.ui.adapter.ViewPageAdapter
import com.luizeduardobrandao.tasksfirebasehilt.ui.viewmodel.HomeViewModel

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

        // TabLayout
        initTabLayout()
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}