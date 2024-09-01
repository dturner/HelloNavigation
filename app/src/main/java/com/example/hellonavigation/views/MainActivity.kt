package com.example.hellonavigation.views

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.Navigation.findNavController
import androidx.navigation.activity
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.hellonavigation.R
import com.example.hellonavigation.databinding.ActivityMainBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

// Routes
@Serializable data object Home
@Serializable data class PlantDetail(val id: String, val name: String)
@Serializable data object AnotherRoute
@Serializable data object HomeGraph
@Serializable data class FruitDetail(val id: String, val fruit: Fruit)

@Serializable
data class Fruit(val name: String, val color: String)

val FruitNavType = object : NavType<Fruit>(false) {
    override fun get(bundle: Bundle, key: String): Fruit? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Fruit {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Fruit) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: Fruit) = Uri.encode(Json.encodeToString(value))
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = binding.navHost.getFragment<NavHostFragment>().navController
        navController.graph = navController.createGraph(
            startDestination = HomeGraph
        ) {
            fragment<PlantDetailFragment, PlantDetail>{
                deepLink {
                    navDeepLink<PlantDetail>(basePath = "www.example.com/plant")
                }
                deepLink {
                    navDeepLink {
                        uriPattern = "http://www.example.com/plants/"
                        action = "android.intent.action.MY_ACTION"
                        mimeType = "image/*"
                    }
                }
                label = resources.getString(R.string.plant_detail_title)
            }
            activity<AnotherRoute> {
                activityClass = AnotherActivity::class
            }
            navigation<HomeGraph>(startDestination = Home){
                fragment<HomeFragment, Home> {
                    label = resources.getString(R.string.home_title)
                }
            }
            fragment<FruitDetailFragment, FruitDetail>(
                typeMap = mapOf(typeOf<Fruit>() to FruitNavType)
            ) {

            }
        }
    }
}

inline fun <reified T: Any> NavController.getCurrentRoute() : T = getBackStackEntry<T>().toRoute<T>()