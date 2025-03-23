package com.example.mvvm.fav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.courotines.model.Product
import com.example.mvvm.local.ProductDatabase
import com.example.mvvm.local.ProductLocalDataSource
import com.example.mvvm.remote.APIClient
import com.example.mvvm.remote.ProductRemoteDataSource
import com.example.mvvm.repo.Repo

class FavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            FavProductsScreen(
                ViewModelProvider(
                    this,
                    FavProductViewModel.FavProductsFactory(
                        Repo.getInstance(
                            ProductRemoteDataSource(APIClient.apiService),
                            ProductLocalDataSource(ProductDatabase.getInstance(this).getProductDao())
                        )
                    )
                ).get(FavProductViewModel::class.java)
            )
        }
    }


    @Composable
    private fun FavProductsScreen (viewModel: FavProductViewModel) {
        viewModel.getFavProducts()

        val productState = viewModel.mutableProduct.collectAsStateWithLifecycle()
        val messageState = viewModel.mutableMessage.collectAsStateWithLifecycle()

        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        Scaffold (
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {
                contentPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp) ,
                verticalArrangement = Arrangement.Center
            ){
                LazyColumn {
                    items(items = productState.value?: listOf()){product->
                        ProductItem(product = product, isFavScreen = true) {
                            viewModel.removeFromFav(it)
                        }

                    }
                }
                LaunchedEffect(messageState.value) {
                    if (!messageState.value.isNullOrBlank()) {
                        snackBarHostState.showSnackbar(
                            message = messageState.value.toString(),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }

        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProductItem(
        product: Product,
        isFavScreen: Boolean,
        onButtonClicked: (Product) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            GlideImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(end = 16.dp),
                model = product.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )

                Button (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    onClick = {
                        onButtonClicked(product)
                    }
                ) {
                    Text(
                        text = if (isFavScreen) "Remove From Fav" else "Add To Fav"
                    )
                }
            }
        }
    }
}

