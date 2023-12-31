package com.example.s_shopping.data.source.remote

import com.example.s_shopping.common.Constants.Endpoint.ADD_CART_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.CLEAR_CART_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.DELETE_CART_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.GET_CART_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.GET_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.GET_PRODUCTS_BY_CATEGORY
import com.example.s_shopping.common.Constants.Endpoint.GET_PRODUCT_DETAIL
import com.example.s_shopping.common.Constants.Endpoint.GET_SALE_PRODUCTS
import com.example.s_shopping.common.Constants.Endpoint.GET_SEARCH_PRODUCT
import com.example.s_shopping.data.model.request.AddToCartRequest
import com.example.s_shopping.data.model.request.ClearCartRequest
import com.example.s_shopping.data.model.request.DeleteFromCartRequest
import com.example.s_shopping.data.model.response.BaseResponse
import com.example.s_shopping.data.model.response.GetProductDetailResponse
import com.example.s_shopping.data.model.response.GetProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

//Interface içine dahil ettiğim fonksiyonlar veri çekme,gönderme işlemleri yapacka.

interface ProductService {

    @GET(GET_PRODUCTS)
    //retrofitin coroutine desteği
    suspend fun getProducts(): Response<GetProductsResponse>
    //Başlatma, bekletme, durdurma işlemlerini suspend keywordu sağlıyor.

    @GET(GET_PRODUCT_DETAIL)
    suspend fun getProductDetail(
        @Query("id") id: Int
    ): Response<GetProductDetailResponse>

    @GET(GET_SALE_PRODUCTS)
    suspend fun getSaleProducts(): Response<GetProductsResponse>

    @GET(GET_PRODUCTS_BY_CATEGORY)
    suspend fun getProductsByCategory(@Query("category") categoryValue: String): GetProductsResponse

    @GET(GET_SEARCH_PRODUCT)
    suspend fun getSearchResult(@Query("query") query: String): Response<GetProductsResponse>

    @GET(GET_CART_PRODUCTS)
    suspend fun getCartProducts(@Query("userId") userId: String): Response<GetProductsResponse>

    @POST(ADD_CART_PRODUCTS)
    suspend fun addProductToCart(@Body request: AddToCartRequest): Response<BaseResponse>

    @POST(DELETE_CART_PRODUCTS)
    suspend fun deleteProductFromCart(@Body request: DeleteFromCartRequest): Response<BaseResponse>

    //body type: veriyi gövdeye koy
    @POST(CLEAR_CART_PRODUCTS)
    suspend fun clearProductFromCart(@Body request: ClearCartRequest): Response<BaseResponse>
}