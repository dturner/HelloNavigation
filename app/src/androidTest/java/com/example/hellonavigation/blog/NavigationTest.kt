package com.example.hellonavigation.blog

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    //val composeTestRule = createAndroidComposeRule<MainActivity>()
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            com.example.hellonavigation.adaptive.AppNavHost(navController = navController)
        }
    }

    // Unit test
    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule
            .onNodeWithText("Screen Home")
            .assertIsDisplayed()
    }

    @Test
    fun onHomeScreen_whenProductIsTapped_thenProductScreenIsDisplayed() {
        composeTestRule.apply {
            onNodeWithText("View details about ABC").performClick()
            onNodeWithText("Product details for ABC").assertExists()
        }
    }

    @Test
    fun appNavHost_clickAllProfiles_navigateToProfiles() {
        composeTestRule.onNodeWithText("View details about ABC")
            .performClick()

        assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<Product>() ?: false)
    }

    @Test
    fun interactive() {
        composeTestRule.waitUntil(timeoutMillis = 3_600_000, condition = { false })
    }

}

