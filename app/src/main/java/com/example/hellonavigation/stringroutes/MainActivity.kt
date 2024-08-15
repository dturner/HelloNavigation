package com.example.hellonavigation.stringroutes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.hellonavigation.ui.theme.HelloNavigationTheme


const val LINKED_NEWS_RESOURCE_ID = "linkedNewsResourceId"
const val FOR_YOU_ROUTE = "for_you_route"
private const val DEEP_LINK_URI_PATTERN =
    "https://www.stringroutes.hellonavigation.example.com/foryou/{$LINKED_NEWS_RESOURCE_ID}"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            HelloNavigationTheme {
                Scaffold{ paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {

                        NavHost(navController = navController, startDestination = FOR_YOU_ROUTE) {
                            composable(
                                route = FOR_YOU_ROUTE,
                                deepLinks = listOf(
                                    navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
                                ),
                                arguments = listOf(
                                    navArgument(LINKED_NEWS_RESOURCE_ID) {
                                        type = NavType.StringType
                                        nullable = true
                                    },
                                ),
                            ) {
                                val newsId = it.arguments?.getString(LINKED_NEWS_RESOURCE_ID)
                                Text("News id $newsId")
                            }
                        }
                    }
                }
            }
        }
    }
}