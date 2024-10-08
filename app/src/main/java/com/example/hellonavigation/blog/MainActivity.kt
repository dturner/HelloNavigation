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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import com.example.hellonavigation.ui.theme.PastelBlue
import com.example.hellonavigation.ui.theme.PastelRed
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

/*enum class AccountType {
    PREMIUM,
    BASIC
}*/

// Routes
@Serializable data object Home
@Serializable data class Product(
    val id: String,
    val colors: List<String> = listOf("red", "blue"),
    val description: String? = null
)
//@Serializable data class Account(val type: AccountType)

private data class TopLevelRoute(val clazz: KClass<*>, val icon: ImageVector)

private val TOP_LEVEL_ROUTES = listOf(
    TopLevelRoute(
        clazz = Home::class,
        icon = Icons.Filled.Home,
    ),
    TopLevelRoute(
        clazz = Product::class,
        icon = Icons.Filled.ShoppingCart
    ),
 /*   TopLevelRoute(
        clazz = Account::class,
        icon = Icons.Filled.AccountCircle
    )*/
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(paddingValues)) {

                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Serializable data class User(val name: String)

@Composable
fun NullNavHost(navController: NavHostController){

    NavHost(navController = navController, startDestination = User(name = "null")){
        composable<User> { backStackEntry ->
            val user = backStackEntry.toRoute<User>()
            Text("User name is: ${user.name}")
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val backStack = navController.currentBackStack.collectAsState()

    val composeNavigator2 = navController.navigatorProvider.get(ComposeNavigator::class)

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                onProductClick = { productId ->
                    navController.navigate(route = Product(productId))
                },
                onAccountClick = { /*accountType ->
                    navController.navigate(route = Account(accountType))*/
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
        /*composable<Account>{ backStackEntry ->
            val account = backStackEntry.toRoute<Account>()
            AccountScreen(account)
        }*/
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth(1.0F)
            .padding(16.dp, 16.dp)
    ){
        val backStack2 = composeNavigator2.backStack.collectAsState()
        MyBackStack(backStack2)
    }

    NavigationBar {
        TOP_LEVEL_ROUTES.forEach { topLevelDestination ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(route = topLevelDestination.clazz)
                } == true,
                icon = {
                    Icon(
                        imageVector = topLevelDestination.icon,
                        contentDescription = topLevelDestination.clazz.simpleName
                    )
                },
                onClick = {}
            )
        }
    }
}

/*
@Composable
fun AccountScreen(account: Account) {
    Text("Account type: ${account.type}")
}
*/


@Composable
private fun MyBackStack(backStack: State<List<NavBackStackEntry>>) {
    Text("Current back stack")
    backStack.value.forEach {
        Text("Route: ${it.destination.route}")
    }
}

@Composable
private fun HomeScreen(onProductClick: (String) -> Unit, onAccountClick: (/*AccountType*/) -> Unit){
    MyScreen(name = "Home", bgColor = PastelRed){
        Column {
            Button(onClick = { onProductClick("ABC") }){
                Text("View details about ABC")
            }
            Button(onClick = { onAccountClick(/*AccountType.PREMIUM*/) }){
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