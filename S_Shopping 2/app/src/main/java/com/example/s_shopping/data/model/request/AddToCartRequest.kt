package com.example.s_shopping.data.model.request

data class AddToCartRequest(
    val userId: String,
    val productId: Int
)