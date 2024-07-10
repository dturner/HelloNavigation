package com.example.hellonavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

import androidx.navigation.compose.rememberNavController
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.serialization.Serializable

// Routes in the app
@Serializable object Title
@Serializable object Register
@Serializable object GameInProgress // Nested graph

@Serializable object Match
@Serializable object InGame
@Serializable object ResultsWinner
@Serializable object GameOver


@Composable
private fun TitleScreen(onPlayClicked: () -> Unit, onLeaderboardsClicked: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Cyan)
    ) {
        Text("Title")
        Button(onClick = onPlayClicked) {
            Text("Play")
        }
    }
}


@Composable
private fun RegisterScreen(onSignUpComplete: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Magenta)
    ) {
        Text("Register")
        Button(onClick = onSignUpComplete) {
            Text("Sign up")
        }
    }
}



@Composable
private fun MatchScreen(onStartGame: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Gray)
    ) {
        Text("Match")
        Button(onClick = onStartGame) {
            Text("Start")
        }
    }
}


@Composable
private fun InGameScreen(onGameWin: () -> Unit, onGameLose: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Blue)
    ) {
        Text("Match")
        Button(onClick = onGameWin) {
            Text("Win")
        }
        Button(onClick = onGameLose) {
            Text("Lose")
        }
    }
}

@Composable
private fun ResultsWinnerScreen(onNextMatchClicked: () -> Unit, onLeaderboardsClicked: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Green)
    ) {
        Text("You won!")
        Button(onClick = onNextMatchClicked) {
            Text("Next match")
        }
    }
}

@Composable
private fun GameOverScreen(onTryAgainClicked: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Red)
    ) {
        Text("You lost!")
        Button(onClick = onTryAgainClicked) {
            Text("Try again")
        }
    }
}

class NestedNavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloNavigationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()

                    NavHost(navController, startDestination = Title) {
                        composable<Title> {
                            TitleScreen(
                                onPlayClicked = { navController.navigate(route = Register) },
                                onLeaderboardsClicked = { /* Navigate to leaderboards */ }
                            )
                        }
                        composable<Register> {
                            RegisterScreen(
                                onSignUpComplete = { navController.navigate(route = GameInProgress) }
                            )
                        }
                        navigation<GameInProgress>(startDestination = Match) {
                            composable<Match> {
                                MatchScreen(
                                    onStartGame = { navController.navigate(route = InGame) }
                                )
                            }
                            composable<InGame> {
                                InGameScreen(
                                    onGameWin = { navController.navigate(route = ResultsWinner) },
                                    onGameLose = { navController.navigate(route = GameOver) }
                                )
                            }
                            composable<ResultsWinner> {
                                ResultsWinnerScreen(
                                    onNextMatchClicked = {
                                        navController.navigate(route = Match) {
                                            popUpTo(route = Match) { inclusive = true }
                                        }
                                    },
                                    onLeaderboardsClicked = { /* Navigate to leaderboards */ }
                                )
                            }
                            composable<GameOver> {
                                GameOverScreen(
                                    onTryAgainClicked = {
                                        navController.navigate(route = Match) {
                                            popUpTo(route = Match) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

