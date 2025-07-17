package com.luizeduardobrandao.tasksfirebasehilt.helper

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luizeduardobrandao.tasksfirebasehilt.R
import com.luizeduardobrandao.tasksfirebasehilt.databinding.FragmentBottomSheetBinding

// * Função para inicialização da Toolbar

fun Fragment.initToolbar(toolbar: Toolbar) {
    val activity = requireActivity() as AppCompatActivity

    activity.setSupportActionBar(toolbar)
    activity.title = ""
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener {
        findNavController().navigateUp()
    }
}


// * Função para exibição do Bottom Sheet

fun Fragment.showBottomSheet(
    titleDialog: Int? = null,
    titleButton: Int? = null,
    message: String,
    onClick: () -> Unit = {}
) {

    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val binding: FragmentBottomSheetBinding =
        FragmentBottomSheetBinding.inflate(layoutInflater, null, false)

    binding.textSheetTitle.text = getText(titleDialog ?: R.string.text_bottom_sheet_warning)
    binding.textSheetMessage.text = message
    binding.btnBottomSheet.text = getText(titleButton ?: R.string.text_btn_bottom_sheet)

    binding.btnBottomSheet.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(binding.root)

    // **Aqui você obriga o sheet a expandir completamente**
    bottomSheetDialog.setOnShowListener { dialog ->
        // pega o BottomSheetBehavior diretamente
        val behavior = (dialog as BottomSheetDialog).behavior.apply {
            isFitToContents = true
            // usa altura total da tela para mostrar t0do o conteúdo
            peekHeight = Resources.getSystem().displayMetrics.heightPixels
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        // opcional: não permitir colapsar ao arrastar pra baixo
        behavior.isDraggable = false
    }
    // Exibir o Bottom Sheet
    bottomSheetDialog.show()
}