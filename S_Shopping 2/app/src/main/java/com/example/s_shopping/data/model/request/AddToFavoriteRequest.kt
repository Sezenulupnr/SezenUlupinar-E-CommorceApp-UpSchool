package com.example.s_shopping.data.model.request

data class AddToFavoriteRequest(
    val userId: String,
    val productId: Int
)