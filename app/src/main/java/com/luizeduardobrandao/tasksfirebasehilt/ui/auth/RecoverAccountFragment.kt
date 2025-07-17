package com.luizeduardobrandao.tasksfirebasehilt.ui.auth

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.ui.auth.viewmodel.RecoverAccountViewModel

class RecoverAccountFragment : Fragment() {

    companion object {
        fun newInstance() = RecoverAccountFragment()
    }

    private val viewModel: RecoverAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recover_account, container, false)
    }
}