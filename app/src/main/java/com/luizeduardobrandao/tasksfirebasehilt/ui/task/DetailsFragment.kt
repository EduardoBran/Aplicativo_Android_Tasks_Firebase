package com.luizeduardobrandao.tasksfirebasehilt.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentDetailsBinding
import com.luizeduardobrandao.tasksfirebasehilt.helper.Status
import com.luizeduardobrandao.tasksfirebasehilt.helper.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    // SafeArgs para receber a Task
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inicializa toolbar com back automático
        initToolbar(binding.toolbarDetails)

        // preenche campos com os dados da Task
        val task = args.task
        binding.textDescription.text = task.description

        // Mapeia o enum Status para o texto em português
        val statusText = when (task.status) {
            Status.TODO  -> getString(R.string.text_status_todo)
            Status.DOING -> getString(R.string.text_status_doing)
            Status.DONE  -> getString(R.string.text_status_done)
        }
        binding.textStatus.text = statusText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}