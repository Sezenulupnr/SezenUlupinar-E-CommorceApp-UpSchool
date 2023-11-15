package com.example.s_shopping.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.s_shopping.common.viewBinding
import com.example.s_shopping.databinding.FragmentFavoritesBinding
import com.example.s_shopping.R
import com.example.s_shopping.common.gone
import com.example.s_shopping.common.visible
import com.example.s_shopping.data.model.response.ProductUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorites), FavoriteAdapter.FavProductListener {

    private val binding by viewBinding(FragmentFavoritesBinding::bind)

    private val viewModel by viewModels<FavoriteViewModel>()

    private val favoriteAdapter by lazy { FavoriteAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()

        with(binding) {
            rvFavorites.adapter = favoriteAdapter
        }

        observeData()

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(FavoriteFragmentDirections.favoriteToHome())
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)
    }

    private fun observeData() = with(binding) {
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesState.Loading -> progressBar.visible()

                is FavoritesState.SuccessState -> {
                    progressBar.gone()
                    favoriteAdapter.submitList(state.products)
                }

                is FavoritesState.EmptyScreen -> {
                    ivError.visible()
                    tvError.visible()
                    tvError.text = state.failMessage
                    rvFavorites.gone()
                    progressBar.gone()

                }

                is FavoritesState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    override fun onProductClick(id: Int) {
        findNavController().navigate(FavoriteFragmentDirections.favoriteToDetail(id))
    }

    override fun onDeleteClick(product: ProductUI) {
        viewModel.deleteFavProduct(product)
    }

}