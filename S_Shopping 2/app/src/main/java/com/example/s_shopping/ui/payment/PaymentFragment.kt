package com.example.s_shopping.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.FragmentPaymentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding(FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnAccent.setOnClickListener {
                viewModel.checkPayment(
                    etCardName.text.toString(),
                    etCardNumber.text.toString(),
                    etCardMonth.text.toString(),
                    etCardYear.text.toString(),
                    etCvv.text.toString(),
                    etCounty.text.toString(),
                    etCity.text.toString(),
                    etAddress.text.toString(),
                )
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.paymentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PaymentState.Loading -> progressBar.visible()

                is PaymentState.SuccessState -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.paymentToResult)
                }

                is PaymentState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
