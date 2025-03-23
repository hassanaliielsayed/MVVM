package com.example.mvvm.local

import androidx.lifecycle.LiveData
import com.example.courotines.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSourceInterface {

    suspend fun getFavProducts(): Flow<List<Product>>

    suspend fun insert(product: Product): Long

    suspend fun delete(product: Product): Int
}