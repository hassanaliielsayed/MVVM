package com.example.mvvm.repo

import androidx.lifecycle.LiveData
import com.example.courotines.model.Product
import com.example.mvvm.local.ProductLocalDataSourceInterface
import com.example.mvvm.remote.ProductRemoteDataSourceInterface
import com.example.mvvm.remote.Result
import kotlinx.coroutines.flow.Flow

class Repo private constructor(
    private val productRemoteDataSource: ProductRemoteDataSourceInterface,
    private val productLocalDataSource: ProductLocalDataSourceInterface
) : ProductRepo{

    override suspend fun getAllProduct(isOnline: Boolean) = productRemoteDataSource.getAllProduct()


    override suspend fun getFavProduct(): Flow<List<Product>> {
        return productLocalDataSource.getFavProducts()
    }

    override suspend fun addProduct(product: Product): Long {
        return productLocalDataSource.insert(product)
    }

    override suspend fun removeProduct(product: Product): Int {
        return productLocalDataSource.delete(product)
    }

    companion object{

        private var INSTANCE: Repo? = null

        fun getInstance(productRemoteDataSource: ProductRemoteDataSourceInterface,
                        productLocalDataSource: ProductLocalDataSourceInterface) :ProductRepo{

            return INSTANCE ?: synchronized(this){
                val temp = Repo(productRemoteDataSource, productLocalDataSource)
                INSTANCE = temp

                temp
            }

        }

    }
}