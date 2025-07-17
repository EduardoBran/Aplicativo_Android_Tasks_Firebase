package com.luizeduardobrandao.tasksfirebasehilt.helper

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentBottomSheetBinding

class BottomSheetFragment(
    private val titleDialog: Int? = null,
    private val titleButton: Int? = null,
    private val message: Int,
    private val onClick: () -> Unit = {}
) : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textSheetTitle.text =
            getText(titleDialog ?: R.string.text_bottom_sheet_warning)
        binding.textSheetMessage.text = getText(message)
        binding.btnBottomSheet.text =
            getText(titleButton ?: R.string.text_btn_bottom_sheet)

        binding.btnBottomSheet.setOnClickListener {
            onClick()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}