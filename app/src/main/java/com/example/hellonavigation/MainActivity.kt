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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellonavigation.ui.theme.HelloNavigationTheme

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
        startDestination = "list"
    ) {
        composable(route = "list") {
            ListScreen(navController = navController)
        }
        composable(route = "myroute") {
            DetailScreen()
        }
    }
}

@Composable
fun ListScreen(navController: NavHostController) {
    Column {
        Text("I am a list screen")
        Button(onClick = { navController.navigate(route = "detail") }){
            Text("Go to detail screen")
        }
    }
}

@Preview
@Composable
fun DetailScreen() {
    Text("I am a detail screen")
}