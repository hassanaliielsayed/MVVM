package com.example.mvvm.remote

import com.example.courotines.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("products")
    suspend fun getAllProducts(): ProductResponse
}