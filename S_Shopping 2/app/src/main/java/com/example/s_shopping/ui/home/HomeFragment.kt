package com.example.s_shopping.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.visible
import com.example.s_shopping.R
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.data.model.response.ProductUI
import com.google.android.material.snackbar.Snackbar
import com.example.s_shopping.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ProductAdapter.ProductListener,
    SalesProductAdapter.ProductListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val viewModel by viewModels<HomeViewModel>()

    private val productAdapter by lazy { ProductAdapter(this) }

    private val saleProductAdapter by lazy { SalesProductAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            //rv ye veriyi verdim
            rvProduct.adapter = productAdapter
            rvDiscountedProducts.adapter = saleProductAdapter

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.rb_all) {
                    viewModel.getProducts()
                } else {
                    //kategoriye göre listeleme
                    val category = when (checkedId) {
                        R.id.rb_waist_bag -> "Bel Çantası"
                        R.id.rb_mini_bag -> "Mini Çanta"
                        R.id.rb_hand_bag -> "El Çantası"
                        R.id.rb_shoulder_bag -> "Omuz Çantası"
                        R.id.rb_case -> "Valiz"
                        R.id.rb_pack_bag -> "Sırt Çantası"
                        R.id.rb_shopper_bag -> "Alışveriş Çantası"
                        else -> "all"
                    }
                    viewModel.getProductsByCategory(category)
                }
            }

            btnAccount.setOnClickListener {
                findNavController().navigate(R.id.homeToAccount)
            }
        }

        viewModel.getProducts()
        viewModel.getSaleProducts()

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                HomeState.Loading -> progressBar.visible()

                is HomeState.SuccessState -> {
                    progressBar.gone()
                    productAdapter.submitList(state.products)
                }

                is HomeState.EmptyScreen -> {
                    progressBar.gone()
                    ivError.visible()
                    tvError.visible()
                    tvError.text = state.failMessage
                    rvProduct.visible()
                    Snackbar.make(requireView(), state.failMessage, 1000).show()

                }

                is HomeState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }

        viewModel.salesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SalesState.Loading -> progressBar.visible()

                is SalesState.SuccessState -> {
                    progressBar.gone()
                    saleProductAdapter.submitList(state.products)
                }

                is SalesState.EmptyScreen -> {
                    progressBar.gone()
                    ivError.visible()
                    tvError.visible()
                    tvError.text = state.failMessage
                }

                is SalesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        findNavController().navigate(HomeFragmentDirections.homeToDetail(id))
    }

    override fun onFavoriteClick(product: ProductUI) {
        viewModel.setFavoriteState(product)
    }
}