package com.example.hellonavigation.shareddestination

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import com.example.hellonavigation.ui.theme.PastelBlue
import com.example.hellonavigation.ui.theme.PastelGreen
import com.example.hellonavigation.ui.theme.PastelOrange
import com.example.hellonavigation.ui.theme.PastelRed
import com.example.hellonavigation.ui.theme.PastelYellow
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
private abstract class Screen(val name: String, val color: ULong)

// Routes
@Serializable private object A : Screen(name = "A", color = PastelRed.value)
@Serializable private object B : Screen(name = "B", color = PastelOrange.value)
@Serializable private class C(val itemId: String? = null) : Screen(name = "C", color = PastelYellow.value)
@Serializable private object C1 : Screen(name = "C1", color = PastelGreen.value)
@Serializable private class C2(val itemId: String) : Screen(name = "C2", color = PastelBlue.value)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val backStack = navController.currentBackStack.collectAsState()

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(paddingValues)) {
                        NavHost(
                            navController = navController,
                            startDestination = A
                        ) {
                            composable<A> {
                                MyScreenA(
                                    onDetailsClick = { navController.navigate(route = C("123")) }
                                )
                            }
                            composable<B> {
                                MyScreenB(
                                    onDetailsClick = { navController.navigate(route = C("456")) }
                                )
                            }
                            composable<C> { outerBackStackEntry ->
                                val routeC = outerBackStackEntry.toRoute<C>()

                                var startDestination : Any = C1
                                if (routeC.itemId != null){
                                    startDestination = C2(itemId = routeC.itemId)
                                }

                                val nestedNavController = rememberNavController()
                                NavHost(navController = nestedNavController,
                                    startDestination = startDestination){
                                    composable<C1>{
                                        MyListScreen(onDetailsClick = {
                                            nestedNavController.navigate(route = C2)
                                        })
                                    }
                                    composable<C2>{ backStackEntry ->
                                        val routeC2 = backStackEntry.toRoute<C2>()
                                        MyDetailsScreen(itemId = routeC2.itemId)
                                    }
                                }
                            }
                        }
                        Row {
                            MyNavItem(navController, A)
                            MyNavItem(navController, B)
                            MyNavItem(navController, C)
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(1.0F)
                                .padding(16.dp, 16.dp)
                        ){
                            MyBackStack(backStack)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : Any> MyNavItem(navController: NavHostController, route: T) {
    Button(
        modifier = Modifier.padding(16.dp, 8.dp),
        onClick = {
            navController.navigate(route = route, navOptions {
                popUpTo<A>{
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            })
    }) {
        Text(route::class.simpleName!!)
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
private fun MyScreenA(onDetailsClick: () -> Unit){
    MyScreen(properties = A){
        Button(onClick = onDetailsClick){
            Text("Go to details screen")
        }
    }
}

@Composable
private fun MyScreenB(onDetailsClick: () -> Unit){
    MyScreen(properties = B){
        Button(onClick = onDetailsClick){
            Text("Go to details screen")
        }
    }
}

@Composable
private fun MyListScreen(onDetailsClick: () -> Unit){
    MyScreen(properties = C1){
        Button(onClick = onDetailsClick){
            Text("Go to details screen")
        }
    }
}

@Composable
private fun MyDetailsScreen(itemId: String){
    MyScreen(properties = C2(itemId)){
        Text("Item ID $itemId")
    }
}

@Composable
private fun MyScreen(properties: Screen, content: @Composable () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(1.0F)
            .fillMaxHeight(0.5F)
            .background(color = Color(properties.color))
    ) {
        Row(
            modifier = Modifier.padding(24.dp, 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Screen ${properties.name}")
        }
        content()
    }
}