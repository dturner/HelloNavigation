package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            HelloNavigationTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            rootDestinations.forEach { rootDestination ->
                                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                                val isSelected = currentBackStackEntry?.destination?.hierarchy?.any {
                                    it.hasRoute(route = rootDestination.route::class)
                                } ?: false

                                NavigationBarItem(
                                    label = { Text(text = rootDestination.description) },
                                    selected = isSelected,
                                    onClick = { navController.navigate(route = rootDestination.route) },
                                    icon = { /*TODO*/ }
                                )
                            }
                        }
                    }

                ) {
                    MyNavHost(navController = navController)
                }

            }
        }
    }
}

@Serializable
private data object HomeNavRoute
@Serializable
private data object HomeRoute
@Serializable
private data object ListNavRoute
@Serializable
private data object ListRoute // #1 Defining routes and their arguments as types
@Serializable
private data class DetailRoute(val id: Int)

private data class RootDestination(val route: Any, val description: String)

private val rootDestinations : List<RootDestination> = listOf(
    RootDestination(route = HomeNavRoute, description = "Home"),
    RootDestination(route = ListNavRoute, description = "List")
)


@Composable
private fun MyNavHost(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxHeight()) {
        NavHost(
            navController = navController,
            startDestination = HomeNavRoute
        ) {
            navigation<ListNavRoute>(startDestination = ListRoute) {
                composable<ListRoute> {
                    ListScreen(onClickItem = {
                        navController.navigate(route = DetailRoute(id = 123))
                    })
                }
                composable<DetailRoute> { backStackEntry ->
                    DetailScreen(
                        id = backStackEntry.toRoute<DetailRoute>().id,
                        parent = backStackEntry.destination.parent)
                }
            }
            navigation<HomeNavRoute>(startDestination = HomeRoute) {
                composable<HomeRoute> {
                    HomeScreen(onClickItem = {
                        navController.navigate(route = DetailRoute(id = 456))
                    })
                }
                composable<DetailRoute> { backStackEntry ->
                    DetailScreen(
                        id = backStackEntry.toRoute<DetailRoute>().id,
                        parent = backStackEntry.destination.parent)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(onClickItem: () -> Unit) {
    Column {
        Text("Home screen")
        Button(onClick = onClickItem){
            Text("Go to detail")
        }
    }
}

@Composable
private fun ListScreen(onClickItem: () -> Unit){
    Column {
        Text("I am a list screen")
        Button(onClick = onClickItem){
            Text("Go to detail screen")
        }
    }
}

@Composable
private fun DetailScreen(id: Int, parent: NavDestination? = null) {
    Text("I am a detail screen with ID $id, my parent is $parent")
}

@Composable
private fun MyTab(title: String, isSelected: Boolean, onClick: () -> Unit,  ){
    val backgroundColor = if (isSelected) Color.Green else Color.LightGray
    Button(onClick = onClick, colors = buttonColors(containerColor = backgroundColor)){
        Text(text=title)
    }
}

