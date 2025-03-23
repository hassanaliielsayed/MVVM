package com.example.mvvm.allproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.courotines.model.Product
import com.example.mvvm.remote.Result
import com.example.mvvm.repo.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repo: ProductRepo) : ViewModel() {

    private val _mutableProduct =  MutableStateFlow<Result<List<Product>>>(Result.Loading)
    val mutableProduct = _mutableProduct.asStateFlow()

    fun getAllProducts() {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val result = repo.getAllProduct(true)
                if (result.products.isNotEmpty()){
                    _mutableProduct.value = Result.Success(result.products)
                } else {
                    _mutableProduct.value = Result.Error("List is Empty")
                }
            } catch (ex: Exception) {
                _mutableProduct.value = Result.Error("An Error Occur : ${ex.message}")
            }

//            try {
//                repo.getAllProduct(true)
//                    .catch { ex -> _mutableProduct.value = Result.Error(ex.message.toString()) }
//                    .collect { result ->
//                        _mutableProduct.value = result
//                    }
//            } catch (ex: Exception) {
//                _mutableProduct.value = Result.Error(ex.message.toString())
//            }


        }
    }

    fun addToFav(product: Product) {

        viewModelScope.launch(Dispatchers.IO) {

            repo.addProduct(product)

        }

//        if (product != null){
//            viewModelScope.launch(Dispatchers.IO) {
//                try {
//                    val result = repo.addProduct(product)
//                    if (result > 0) {
//                        mutableMessage.postValue("Added Successfully :)")
//                    } else {
//                        mutableMessage.postValue("Product is Already exist :(")
//                    }
//                } catch (ex: Exception) {
//                    mutableMessage.postValue("Could not add Product ${ex.message}")
//                }
//            }
//        } else {
//            mutableMessage.postValue("Could not add Product")
//        }

    }


    class AllProductsFactory(private val repo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllProductsViewModel(repo) as T
        }
    }
}