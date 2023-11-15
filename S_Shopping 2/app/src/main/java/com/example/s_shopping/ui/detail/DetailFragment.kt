package com.example.s_shopping.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.common.visible
import com.example.s_shopping.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel by viewModels<DetailViewModel>()

    private val detailAdapter by lazy { DetailAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProductDetail(args.id)

        with(binding) {

            rvDetailImage.adapter = detailAdapter

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnShop.setOnClickListener {
                viewModel.addProductToCart(args.id)
                Snackbar.make(requireView(), "Ürün sepete eklendi!", 1000).show()
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailState.Loading -> progressBar.visible()

                is DetailState.SuccessState -> {
                    val imageList = listOf(
                        state.product.imageOne,
                        state.product.imageTwo,
                        state.product.imageThree
                    )
                    detailAdapter.updateList(imageList)
                    progressBar.gone()
                    val scaledRating =
                        (state.product?.rate?.toFloat()!! / 5.0f) * 100.0f // Rate değeri
                    ratingBar.rating = scaledRating / 20.0f

                    productName.text = state.product.title
                    productCategory.text = state.product.category
                    productFeature.text = state.product.description

                    if (state.product.saleState == true) {
                        productPrice.isVisible = true
                        productPrice.text = "${state.product.salePrice} ₺"
                    } else {
                        productPrice.text = "${state.product.price} ₺"
                        productPrice.isVisible = true
                    }

                    btnFav.setBackgroundResource(
                        if (state.product.isFav) R.drawable.favorite_icon
                        else R.drawable.favorite_icon_unselected
                    )

                    btnFav.setOnClickListener {
                        viewModel.setFavoriteState(state.product)
                    }
                }

                is DetailState.EmptyScreen -> {
                    progressBar.gone()
                    ivError.visible()
                    tvError.visible()
                    tvError.text = state.failMessage
                }

                is DetailState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
