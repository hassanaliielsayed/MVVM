package com.example.mvvm.remote

import com.example.courotines.model.ProductResponse

interface ProductRemoteDataSourceInterface {

    suspend fun getAllProduct(): ProductResponse
}