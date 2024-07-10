package com.example.hellonavigation.nia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

@Serializable
private data object ForYouRoute
@Serializable
private data object InterestsRoute


class NiaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            val backStack = navController.currentBackStack.collectAsState()

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

                ) { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        MyNavHost(navHostController = navController)
                        Text("Current back stack")
                        backStack.value.forEach {
                            Text("Route: ${it.destination.route}")
                        }
                    }
                }
            }
        }
    }
}


private data class RootDestination(val route: Any, val description: String)

private val rootDestinations : List<RootDestination> = listOf(
    RootDestination(route = ForYouRoute, description = "For You"),
    RootDestination(route = InterestsRoute, description = "Interests")
)


@Composable
private fun MyNavHost(navHostController: NavHostController) {
    Column {
        NavHost(
            navController = navHostController,
            startDestination = ForYouRoute
        ) {
            composable<ForYouRoute> {
                MyScreen(onClickItem = {
                    navHostController.navigate(route = InterestsRoute)
                })
            }
            composable<InterestsRoute> { backStackEntry ->
                InterestsScreen(onClickItem = {})
            }
        }
    }
}

@Composable
private fun MyScreen(onClickItem: () -> Unit) {
    Column {
        Text("Home screen")
        Button(onClick = onClickItem){
            Text("Go to detail")
        }

    }
}

@Composable
private fun InterestsScreen(onClickItem: () -> Unit){
    Column {
        Text("List of topics")
        Button(onClick = onClickItem){
            Text("Go to topic detail screen")
        }
    }
}

@Composable
private fun DetailScreen(id: Int, parent: NavDestination? = null) {
    Text("I am a detail screen with ID $id, my parent is $parent")
}

@Composable
fun MyTab(title: String, isSelected: Boolean, onClick: () -> Unit,  ){
    val backgroundColor = if (isSelected) Color.Green else Color.LightGray
    Button(onClick = onClick, colors = buttonColors(containerColor = backgroundColor)){
        Text(text=title)
    }
}

