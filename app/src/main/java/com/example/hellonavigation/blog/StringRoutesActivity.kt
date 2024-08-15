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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import com.example.hellonavigation.ui.theme.PastelBlue
import com.example.hellonavigation.ui.theme.PastelRed
import kotlinx.serialization.Serializable

// Routes
const val HOME_ROUTE = "home"
const val PRODUCT_ID_KEY = "id"
const val PRODUCT_BASE_ROUTE = "product/"
const val PRODUCT_ROUTE = "$PRODUCT_BASE_ROUTE{$PRODUCT_ID_KEY}"

class StringRoutesActivity : ComponentActivity() {
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
                            startDestination = HOME_ROUTE
                        ) {
                            composable(route = HOME_ROUTE) {
                                HomeScreen(
                                    onProductClick = { productId ->
                                        navController.navigate(route = "$PRODUCT_BASE_ROUTE$productId")
                                    }
                                )
                            }
                            composable(
                                route = PRODUCT_ROUTE,
                                arguments = listOf(
                                    navArgument(PRODUCT_ID_KEY) {
                                        type = NavType.StringType
                                        nullable = false
                                    }
                                )
                            ) { entry ->
                                val id = entry.arguments?.getString(PRODUCT_ID_KEY)
                                ProductScreen(id = id ?: "Not found")
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
private fun ProductScreen(id: String){
    MyScreen(name = "Product", bgColor = PastelBlue){
        Text("Product details for $id")
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