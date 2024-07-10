package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloNavigationTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home2(usernames = listOf(""))) {
        composable<Home2> { backStackEntry ->
            val home: Home2 = backStackEntry.toRoute()
            for (username in home.usernames) {
                Text("Username is: $username")
            }
        }
    }
}

@Serializable
data class Home2(
    val usernames: List<String>,
)

@Preview(showBackground = true)
@Composable
fun NavigationPreview() {
    HelloNavigationTheme {
        Content()
    }
}