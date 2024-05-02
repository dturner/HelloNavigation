package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloNavigationTheme {
                MyNavHost()
            }
        }
    }
}

@Serializable
data object ListRoute // #1 Defining routes and their arguments as types
@Serializable
data class DetailRoute(val id: Int)

@Preview
@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ListRoute
    ) {
        composable<ListRoute> {
            ListScreen(onClickItem = {
                navController.navigate(route = DetailRoute(it)) // #3 Navigating to a destination
            })
        }
        composable<DetailRoute> { backStackEntry -> // #2 Defining a destination
            DetailScreen(id = backStackEntry.toRoute<DetailRoute>().id) // #4 Obtaining the route arguments from the navStackBackEntry
        }
    }
}

@Composable
fun ListScreen(onClickItem: (Int) -> Unit){
    Column {
        Text("I am a list screen")
        Button(onClick = { onClickItem(123) }){
            Text("Go to detail screen")
        }
    }
}

@Composable
fun DetailScreen(id: Int) {
    Text("I am a detail screen with ID $id")
}

