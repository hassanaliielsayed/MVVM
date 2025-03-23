package com.example.mvvm.local

import androidx.lifecycle.LiveData
import com.example.courotines.model.Product
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSource(private val productDao: ProductDao): ProductLocalDataSourceInterface {
    override suspend fun getFavProducts(): Flow<List<Product>> {
        return productDao.getFavProducts()
    }

    override suspend fun insert(product: Product): Long {
        return productDao.insert(product)
    }

    override suspend fun delete(product: Product): Int {
        return productDao.delete(product)
    }
}