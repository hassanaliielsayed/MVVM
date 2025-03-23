package com.example.mvvm.repo

import androidx.lifecycle.LiveData
import com.example.courotines.model.Product
import com.example.courotines.model.ProductResponse
import com.example.mvvm.remote.Result
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun getAllProduct(isOnline: Boolean): ProductResponse

    suspend fun getFavProduct(): Flow<List<Product>>

    suspend fun addProduct(product: Product): Long

    suspend fun removeProduct(product: Product): Int
}