package com.example.s_shopping.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s_shopping.common.Resource
import com.example.s_shopping.data.repository.AuthRepository
import com.example.s_shopping.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    private fun clearCart() = viewModelScope.launch {
        val result = productRepository.clearProductFromCart(authRepository.userId())
        _paymentState.value = when (result) {
            is Resource.Success -> PaymentState.SuccessState
            else -> PaymentState.ShowPopUp("İşlem Başarısız")
        }
    }

    fun checkPayment(
        cardName: String,
        cardNumber: String,
        cardMonth: String,
        cardYear: String,
        cvv: String,
        city: String,
        county: String,
        address: String
    ) = viewModelScope.launch {
        _paymentState.value = PaymentState.Loading

        val check = when {
            cardName.isEmpty() -> false
            cardNumber.isEmpty() -> false
            cardNumber.length != 16 -> false
            cardMonth.isEmpty() -> false
            cardYear.isEmpty() -> false
            cardMonth.length != 2 -> false
            cardYear.length != 4 -> false
            cvv.isEmpty() -> false
            cvv.length != 3 -> false
            city.isEmpty() -> false
            county.isEmpty() -> false
            address.isEmpty() -> false
            else -> true
        }

        if (check != true) {
            _paymentState.value = PaymentState.ShowPopUp("Bilgilerinizi kontrol ediniz.")

        } else {
            clearCart()
        }
    }
}

sealed interface PaymentState {
    object Loading : PaymentState
    object SuccessState : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}