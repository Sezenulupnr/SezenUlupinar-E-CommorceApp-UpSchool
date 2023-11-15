package com.example.s_shopping.ui.card

import com.example.s_shopping.common.Resource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s_shopping.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.s_shopping.data.model.response.ProductUI
import com.example.s_shopping.data.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    private var _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> get() = _totalAmount

    fun getCartProducts() = viewModelScope.launch {
        _cartState.value = CartState.Loading

        when (val result = productRepository.getCartProducts(authRepository.userId())) {
            is Resource.Success -> {
                _cartState.value = CartState.SuccessState(result.data)
                _totalAmount.value = result.data.sumOf { product ->
                    if (product.saleState) {
                        product.salePrice
                    } else {
                        product.price
                    }
                }
            }
            is Resource.Error -> {
                _cartState.value = CartState.ShowPopUp(result.errorMessage)
            }
            is Resource.Fail -> {
                _cartState.value = CartState.EmptyScreen(result.failMessage)
            }
        }
    }

    fun deleteProduct(productId: Int, price: Double) = viewModelScope.launch {
        productRepository.deleteProductFromCart(authRepository.userId(), productId)
        _totalAmount.value = _totalAmount.value?.minus(price)
        getCartProducts()
        if (productId == 0) {
            _totalAmount.value = 0.0
        } else {
            totalAmount
        }
    }

    fun clearProduct() = viewModelScope.launch {
        productRepository.clearProductFromCart(authRepository.userId())
        getCartProducts()
        _totalAmount.value = 0.0
    }
}

sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
}