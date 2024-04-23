package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.ExperimentalSafeArgsApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

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

@OptIn(ExperimentalSafeArgsApi::class)
@Preview
@Composable
fun MyNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoute.MyList
    ) {
        composable<NavRoute.MyList> {
            ListScreen(navController = navController)
        }
        composable<NavRoute.MyDetail> {
            DetailScreen()
        }
    }
}

@OptIn(ExperimentalSafeArgsApi::class)
@Composable
fun ListScreen(navController: NavHostController) {
    Column {
        Text("I am a list screen")
        Button(onClick = { navController.navigate(route = NavRoute.MyDetail) }){
            Text("Go to detail screen")
        }
    }

}

@Preview
@Composable
fun DetailScreen() {
    Text("I am a detail screen")
}

sealed interface NavRoute {
    @Serializable
    data object MyList : NavRoute
    @Serializable
    data object MyDetail : NavRoute
}