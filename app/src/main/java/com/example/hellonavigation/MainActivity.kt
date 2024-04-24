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
            ListScreen(onClickItem = {
                navController.navigate(NavRoute.MyDetail(it))
            })
        }
        composable<NavRoute.MyDetail> {
            DetailScreen(it.toRoute<NavRoute.MyDetail>().id)
        }
    }
}

@Composable
fun ListScreen(onClickItem: (Int) -> Unit){
    Column {
        Text("I am a list screen")
        Button(onClick = { onClickItem(1) }){
            Text("Go to detail screen")
        }
    }

}

@Composable
fun DetailScreen(id: Int) {
    Text("I am a detail screen with ID $id")
}

sealed interface NavRoute {
    @Serializable
    data object MyList : NavRoute
    @Serializable
    data class MyDetail(val id: Int) : NavRoute
}