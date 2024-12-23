package com.sametozbalkan.dummyproject.service

import com.sametozbalkan.dummyproject.modal.Product
import com.sametozbalkan.dummyproject.modal.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    suspend fun getProducts(): ProductListResponse

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product
}