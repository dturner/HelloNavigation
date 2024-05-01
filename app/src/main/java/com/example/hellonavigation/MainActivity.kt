package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hellonavigation.ui.theme.HelloNavigationTheme


const val DETAIL_ID_ARG = "detailId"
const val LIST_ROUTE = "list"
const val DETAIL_ROUTE = "detail/{${DETAIL_ID_ARG}}"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloNavigationTheme {
                MyNavGraph()
            }
        }
    }
}

@Preview
@Composable
fun MyNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LIST_ROUTE
    ) {
        composable(route = LIST_ROUTE) {
            ListScreen(navController = navController)
        }
        composable(
            route = DETAIL_ROUTE,
            arguments = listOf(
                navArgument(DETAIL_ID_ARG) { type = NavType.StringType }
            )
        ) {
            DetailScreen(id = it.arguments?.getString(DETAIL_ID_ARG))
        }
    }
}

@Composable
fun ListScreen(navController: NavHostController) {
    Column {
        Text("I am a list screen")
        Button(onClick = { navController.navigate(route = "detail/123") }){
            Text("Go to detail screen")
        }
    }
}

@Composable
fun DetailScreen(id: String?) {
    Text("I am a detail screen with ID: $id")
}