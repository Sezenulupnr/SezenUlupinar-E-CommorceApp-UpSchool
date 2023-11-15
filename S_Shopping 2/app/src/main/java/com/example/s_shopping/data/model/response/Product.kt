package com.example.s_shopping.data.model.response

data class  Product(
    val id: Int?,
    val title: String?,
    val price: Double?,
    val salePrice: Double?,
    val description: String?,
    val category: String?,
    val imageOne: String?,
    val imageTwo: String?,
    val imageThree: String?,
    val rate: Double?,
    val count: Int?,
    val saleState: Boolean?
)
// @SerializedName("sale_price") val salePrice: Double?
// @SerializedName("ürün_action") val productAction: Double?