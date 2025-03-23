package com.example.mvvm.allproducts

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.courotines.model.Product
import com.example.mvvm.fav.FavActivity
import com.example.mvvm.local.ProductDatabase
import com.example.mvvm.local.ProductLocalDataSource
import com.example.mvvm.remote.APIClient
import com.example.mvvm.remote.ProductRemoteDataSource
import com.example.mvvm.remote.Result
import com.example.mvvm.repo.Repo
import com.example.mvvm.search.SearchActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllProductsScreen(
                viewModel = ViewModelProvider(
                    this,
                    AllProductsViewModel.AllProductsFactory(
                        Repo.getInstance(
                            ProductRemoteDataSource(APIClient.apiService),
                            ProductLocalDataSource(ProductDatabase.getInstance(this).getProductDao())
                        )
                    )
                ).get(AllProductsViewModel::class.java)
            )
        }
    }


    @Composable
    private fun AllProductsScreen(viewModel: AllProductsViewModel) {
        // Observe the product state from the ViewModel
        val productState by viewModel.mutableProduct.collectAsStateWithLifecycle()

        // Fetch products when the screen is first displayed
        LaunchedEffect(Unit) {
            viewModel.getAllProducts()
        }

        // Scaffold for the screen layout
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                // Button to navigate to the Favorites screen
//            Button(
//                onClick = {
//                    // Navigate to the Favorites screen
//
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Move To Fav Screen")
//            }

                Spacer(modifier = Modifier.height(16.dp))

                // Display UI based on the product state
                when (productState) {
                    is Result.Loading -> {
                        LoadingScreen()
                    }
                    is Result.Success<*> -> {
                        val products = (productState as Result.Success<*>).data as List<Product>
                        ProductList(products = products, viewModel = viewModel)
                    }
                    is Result.Error -> {
                        val errorMessage = (productState as Result.Error).message
                        ErrorScreen(errorMessage = errorMessage)
                    }
                }
            }
        }




    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ErrorScreen(errorMessage: String) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 18.sp
            )
        }
    }

    @Composable
    fun ProductList(products: List<Product>, viewModel: AllProductsViewModel) {
        Row {
            Button(
                onClick = {
                    startActivity(Intent(this@MainActivity, FavActivity::class.java))
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Move")
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Button(
                onClick = {
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Move 2")
            }


        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product, viewModel = viewModel)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun ProductItem(product: Product, viewModel: AllProductsViewModel) {

        Card (
            modifier = Modifier.padding(8.dp)
                .shadow(8.dp, MaterialTheme.shapes.large)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {

                GlideImage(
                    model = product.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier
                        .size(125.dp)
                        .padding(8.dp)
                        .clip(
                            CircleShape
                        )
                )
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {

                    Text(
                        text = product.title,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Monospace,
                        color = Color.Red
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                    Text(text = product.description, fontSize = 18.sp)

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.addToFav(product)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add to Favorites")
                    }

                }



            }
        }
    }

}
