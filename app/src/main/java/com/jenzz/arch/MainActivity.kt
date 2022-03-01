package com.jenzz.arch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jenzz.arch.mvi.MviScreen
import com.jenzz.arch.mvvm.update.MvvmUpdateSettingsScreen
import com.jenzz.arch.ui.theme.ArchitectureExperimentsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArchitectureExperimentsTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Surface {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainScreen(
                    onMvvmClick = { navController.navigate("mvvm") },
                    onMviClick = { navController.navigate("mvi") }
                )
            }
            composable("mvvm") {
                MvvmUpdateSettingsScreen()
            }
            composable("mvi") {
                MviScreen()
            }
        }
    }
}

@Composable
private fun MainScreen(
    onMvvmClick: () -> Unit,
    onMviClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        Button(onClick = onMvvmClick) {
            Text("MVVM")
        }
        Button(onClick = onMviClick) {
            Text("MVI")
        }
    }
}
