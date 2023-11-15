package com.example.s_shopping.ui.card

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardFragment : Fragment(R.layout.fragment_cart), CartAdapter.CartProductListener {

    private val binding by viewBinding(FragmentCartBinding::bind)

    private val viewModel by viewModels<CartViewModel>()

    private val cartAdapter by lazy { CartAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCartProducts()

        with(binding) {

            rvCartProducts.adapter = cartAdapter

            btnClear.setOnClickListener {
                viewModel.clearProduct()
            }

            btnBuy.setOnClickListener {
                findNavController().navigate(R.id.cartToPayment)
            }
        }

        observeData()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.cartToHome)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun observeData() = with(binding) {
        viewModel.cartState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CartState.Loading -> progressBar.visible()

                is CartState.SuccessState -> {
                    progressBar.gone()
                    cartAdapter.submitList(state.products)
                    rvCartProducts.isVisible = state.products.isNotEmpty()

                    if (state.products.isEmpty()) {
                        rvCartProducts.gone()
                        tvTotal.gone()
                        tvAmount.gone()
                        btnClear.gone()
                        btnBuy.gone()
                        tvError.visible()
                        tvError.setText("Sepetinizde hiç ürün yok!")
                    } else {
                        rvCartProducts.visibility = View.VISIBLE
                        tvTotal.visible()
                        tvAmount.visible()
                        btnClear.visible()
                        btnBuy.visible()
                        tvError.gone()
                    }
                }

                is CartState.EmptyScreen -> {
                    progressBar.gone()
                    ivError.visible()
                    tvError.visible()
                    tvError.text = state.failMessage
                    tvTotal.gone()
                    tvAmount.gone()
                    btnClear.gone()
                    btnBuy.gone()
                    val list = ArrayList<ProductUI>()
                    cartAdapter.submitList(list)
                }

                is CartState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) {
            tvTotal.text = String.format("%.3f ₺", it)
        }
    }

    override fun onProductClick(id: Int) {
        findNavController().navigate(CardFragmentDirections.cartToDetail(id))
    }

    override fun onDeleteClick(productId: Int, price: Double) {
        viewModel.deleteProduct(productId, price)
    }
}