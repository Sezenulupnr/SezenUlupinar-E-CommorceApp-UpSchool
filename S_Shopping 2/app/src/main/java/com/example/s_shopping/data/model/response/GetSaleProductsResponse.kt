package com.example.s_shopping.data.model.response

data class GetSaleProductsResponse(
    val products: List<Product>?,
    val status: Int?,
    val message: String?
)
