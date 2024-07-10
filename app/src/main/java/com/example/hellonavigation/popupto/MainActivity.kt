package com.example.hellonavigation.popupto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable private data object A
@Serializable private data object B
@Serializable private data object C

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            val backStack = navController.currentBackStack.collectAsState()

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        MyNavHost(navHostController = navController)
                        MyNavBar(navController)
                        MyBackStack(backStack)
                    }
                }
            }
        }
    }
}

@Composable
private fun MyNavHost(navHostController: NavHostController) {
    Column {
        NavHost(
            navController = navHostController,
            startDestination = A
        ) {
            composable<A> { MyScreen(name = "A") }
            composable<B> { MyScreen(name = "B") }
            composable<C> { MyScreen(name = "C") }
        }
    }
}

@Composable
private fun MyNavBar(navController: NavHostController) {
    Column{

        Row {
            MyNavItem(navController, A)
            MyNavItem(navController, B)
            MyNavItem(navController, C)
        }
        val popupTo = navController.graph.findStartDestination()
        Button(onClick = { navController.navigate(route = A, builder = {
            popUpTo(popupTo.id)
        })
        }) {
            Text("A popupto ${popupTo.route}")
        }
    }

}

@Composable
private fun <T : Any> MyNavItem(navController: NavHostController, route: T) {
    Column {
        Button(onClick = { navController.navigate(route = route) }) {
            Text(route::class.simpleName!!)
        }
        Button(onClick = { navController.navigate(route = route, builder = {
            popUpTo<A>{
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }) }) {
            Text("${route::class.simpleName!!} popupto A")
        }
        Button(onClick = { navController.navigate(route = route, builder = { popUpTo<B>{
            inclusive = true
        } }) }) {
            Text("${route::class.simpleName!!} popupto B")
        }
        Button(onClick = { navController.navigate(route = route, builder = { popUpTo<C>{
            inclusive = true
        } }) }) {
            Text("${route::class.simpleName!!} popupto C")
        }
    }
}

@Composable
private fun MyBackStack(backStack: State<List<NavBackStackEntry>>) {
    Text("Current back stack")
    backStack.value.forEach {
        Text("Route: ${it.destination.route}")
    }
}

@Composable
private fun MyScreen(name: String) {
    Column {
        Text(name)
    }
}