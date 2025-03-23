package com.example.mvvm.fav

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.courotines.model.Product
import com.example.mvvm.repo.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavProductViewModel (private val repo: ProductRepo) : ViewModel() {

    private val _mutableProduct = MutableStateFlow<List<Product>>(listOf())
    val mutableProduct = _mutableProduct.asStateFlow()

    private val _mutableMessage =  MutableStateFlow<String>("")
    val mutableMessage = _mutableMessage.asStateFlow()

     fun getFavProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavProduct().collect{
                _mutableProduct.value = it
            }
        }


    }


    fun removeFromFav (product: Product?){
        if (product != null){
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repo.removeProduct(product)
                    _mutableMessage.value = "Removed Successfully :)"

                } catch (ex: Exception) {
                    _mutableMessage.value = "Could not remove Product ${ex.message}"
                }
            }
        } else {
            _mutableMessage.value = ("Could not remove Product")
        }

    }

    class FavProductsFactory(private val repo: ProductRepo): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavProductViewModel(repo) as T
        }
    }
}