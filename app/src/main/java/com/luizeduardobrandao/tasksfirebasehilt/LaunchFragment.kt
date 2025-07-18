package com.luizeduardobrandao.tasksfirebasehilt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.luizeduardobrandao.tasksfirebasehilt.domain.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LaunchFragment : Fragment(R.layout.fragment_launch) {

    // ← Hilt vai preencher esse campo pra você
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // usa a interface do repositório, não FirebaseHelper direto
        val dest = if (authRepository.isAuthenticated()) {
            R.id.homeFragment
        } else {
            R.id.loginFragment
        }

        findNavController().navigate(
            dest,
            null,
            navOptions {
                popUpTo(R.id.launchFragment) { inclusive = true }
            }
        )
    }
}
