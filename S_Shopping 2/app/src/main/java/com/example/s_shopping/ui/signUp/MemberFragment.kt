package com.example.s_shopping.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.FragmentMemberBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberFragment : Fragment(R.layout.fragment_member) {

    private val binding by viewBinding(FragmentMemberBinding::bind)

    private val viewModel by viewModels<MemberViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnMember.setOnClickListener {
                viewModel.signUp(
                    etMailMember.text.toString(),
                    etPasswordMember.text.toString()
                )
            }
            buttonInput.setOnClickListener {
                findNavController().navigate(R.id.inputFragment)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.Loading -> progressBar.visible()

                is SignUpState.GoToHome -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.memberToHome)
                }

                is SignUpState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}