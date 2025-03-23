package com.example.mvvm.remote

import com.example.courotines.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ProductRemoteDataSource(private val service: ProductService) :
    ProductRemoteDataSourceInterface {
    override suspend fun getAllProduct()  = service.getAllProducts()













//    = flow {
//
//        emit(Result.Loading)
//        try {
//            emit(Result.Success(service.getAllProducts().products))
//        } catch (e: IOException) {
//            emit(Result.Error("Network error: ${e.message}"))
//        } catch (e: HttpException) {
//            emit(Result.Error("HTTP error: code: ${e.code()} & message: ${e.message}"))
//        } catch (e: Exception) {
//            emit(Result.Error("An unexpected error occurred: {${e.message}}"))
//        }
//
//    }


}