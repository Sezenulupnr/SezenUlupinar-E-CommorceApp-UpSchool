package com.example.s_shopping.ui.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.FragmentInputBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputFragment : Fragment(R.layout.fragment_input) {

    private val binding by viewBinding(FragmentInputBinding::bind)

    private val viewModel by viewModels<InputViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnInput.setOnClickListener {
                viewModel.signIn(
                    etMail.text.toString(),
                    etPassword.text.toString()
                )
            }

            buttonMember.setOnClickListener {
                findNavController().navigate(R.id.memberFragment)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signInState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignInState.Loading -> progressBar.visible()

                is SignInState.GoToHome -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.inputToHome)
                }

                is SignInState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}