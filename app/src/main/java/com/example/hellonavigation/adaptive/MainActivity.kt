package com.example.hellonavigation.adaptive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import com.example.hellonavigation.ui.theme.PastelBlue
import com.example.hellonavigation.ui.theme.PastelRed
import kotlinx.serialization.Serializable

// Routes
@Serializable data object HomeNav
@Serializable data class Product(
    val id: String,
    val colors: List<String> = listOf("red", "blue"),
    val description: String? = null
)

@Serializable data object Home
@Serializable data object ShoppingCart
@Serializable data object Account

data class TopLevelRoute<T : Any>(val route: T, val icon: ImageVector)

val TOP_LEVEL_ROUTES = listOf(
    TopLevelRoute(route = HomeNav, icon = Icons.Filled.Home),
    TopLevelRoute(route = ShoppingCart, icon = Icons.Filled.ShoppingCart),
    TopLevelRoute(route = Account, icon = Icons.Filled.AccountCircle)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    NavigationSuiteScaffold(
                        navigationSuiteItems = {
                            TOP_LEVEL_ROUTES.forEach { topLevelDestination ->
                                item(
                                    selected = currentDestination?.hierarchy?.any {
                                        it.hasRoute(route = topLevelDestination.route::class)
                                    } == true,
                                    icon = {
                                        Icon(
                                            imageVector = topLevelDestination.icon,
                                            contentDescription = topLevelDestination.route::class.simpleName
                                        )
                                    },
                                    onClick = { navController.navigate(route = topLevelDestination.route)}
                                )
                            }
                        },
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController){

    val backStack = navController.currentBackStack.collectAsState()

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth(1.0F)
    ){
        NavHost(
            navController = navController,
            startDestination = HomeNav
        ) {
            navigation<HomeNav>(startDestination = Home) {
                composable<Home> {
                    HomeScreen(
                        onProductClick = { productId ->
                            navController.navigate(route = Product(productId))
                        },
                        onAccountClick = {
                            navController.navigate(route = Account)
                        }
                    )
                }
                composable<Product>(
                    deepLinks = listOf(
                        navDeepLink<Product>(
                            basePath = "www.hellonavigation.example.com/product",
                        )
                    ),
                ) { backStackEntry ->
                    println(backStackEntry.destination.route)
                    println(backStackEntry.destination.arguments)

                    val product : Product = backStackEntry.toRoute()
                    ProductScreen(product)
                }
            }

            composable<ShoppingCart> {
                Text("Your shopping cart")
            }
            composable<Account>{
                AccountScreen()
            }
        }
        MyBackStack(backStack)
    }
}

@Composable
fun AccountScreen() {
    Text("Account")
}


@Composable
private fun MyBackStack(backStack: State<List<NavBackStackEntry>>) {
    Column(modifier = Modifier.padding(16.dp, 16.dp)){
        Text("Current back stack")
        backStack.value.forEach {
            Text("Route: ${it.destination.route}")
        }
    }
}

@Composable
private fun HomeScreen(onProductClick: (String) -> Unit, onAccountClick: () -> Unit){
    MyScreen(name = "Home", bgColor = PastelRed){
        Column {
            Button(onClick = { onProductClick("ABC") }){
                Text("View details about ABC")
            }
            Button(onClick = { onAccountClick() }){
                Text("View account details")
            }
        }
    }
}

@Composable
private fun ProductScreen(product: Product){
    MyScreen(name = "Product", bgColor = PastelBlue){
        Column {
            Text("Product details for ${product.id}")
            var colors = "Colors: "
            product.colors.forEach {
                colors += "$it "
            }
            Text(colors)
            /*Text("In sale? ${product.isOnSale}")*/
            Text("Description: ${product.description}")
        }

    }
}

@Composable
private fun MyScreen(name: String, bgColor: Color, content: @Composable () -> Unit = {}) {

    val thing = remember {
        MyState(name)
    }

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

class MyState(val name: String)