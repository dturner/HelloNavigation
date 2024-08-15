package com.example.hellonavigation.blog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import com.example.hellonavigation.ui.theme.PastelBlue
import com.example.hellonavigation.ui.theme.PastelRed
import kotlinx.serialization.Serializable

// Routes
@Serializable data object Home
@Serializable data class Product(val id: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStack = navController.currentBackStack.collectAsState()

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(paddingValues)) {
                        NavHost(
                            navController = navController,
                            startDestination = Home
                        ) {
                            composable<Home> {
                                HomeScreen(
                                    onProductClick = { productId ->
                                        navController.navigate(route = Product(productId))
                                    }
                                )
                            }
                            composable<Product>(
                                deepLinks = listOf(
                                    navDeepLink<Product>(
                                        basePath = "example.com/product"
                                    )
                                )
                            ) { backStackEntry ->
                                val product : Product = backStackEntry.toRoute()
                                ProductScreen(product)
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(1.0F)
                                .padding(16.dp, 16.dp)
                        ){
                            MyBackStack(backStack)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MyBackStack(backStack: State<List<NavBackStackEntry>>) {
    Text("Current back stack")
    backStack.value.forEach {
        Text("Route: ${it.destination.route}")
    }
}

@Composable
private fun HomeScreen(onProductClick: (String) -> Unit){
    MyScreen(name = "Home", bgColor = PastelRed){
        Button(onClick = { onProductClick("ABC") }){
            Text("Go to product screen")
        }
    }
}

@Composable
private fun ProductScreen(product: Product){
    MyScreen(name = "Product", bgColor = PastelBlue){
        Text("Product details for ${product.id}")
    }
}

@Composable
private fun MyScreen(name: String, bgColor: Color, content: @Composable () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(1.0F)
            .fillMaxHeight(0.5F)
            .background(color = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(24.dp, 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Screen $name")
        }
        content()
    }
}