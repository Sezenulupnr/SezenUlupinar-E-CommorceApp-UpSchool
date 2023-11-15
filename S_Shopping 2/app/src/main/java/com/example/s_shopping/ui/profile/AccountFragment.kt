package com.example.s_shopping.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding by viewBinding(FragmentAccountBinding::bind)

    private val viewModel by viewModels<AccountViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnOut.setOnClickListener {
                viewModel.logOut()
                findNavController().navigate(R.id.memberFragment)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.accountState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AccountState.Loading -> {
                    progressBar.visible()
                }

                is AccountState.GoToSignIn -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.memberFragment)
                }
            }
        }
    }

}