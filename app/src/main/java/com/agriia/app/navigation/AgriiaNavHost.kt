package com.agriia.app.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agriia.app.core.di.AppContainer
import com.agriia.app.core.di.AgriiaViewModelFactory
import com.agriia.app.feature.chat.ui.ChatScreen
import com.agriia.app.feature.chat.ui.ChatViewModel
import com.agriia.app.feature.splash.SplashScreen

@Composable
fun AgriiaNavHost(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AgriiaDestinations.SPLASH_ROUTE,
        modifier = modifier
    ) {
        composable(
            route = AgriiaDestinations.SPLASH_ROUTE,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(AgriiaDestinations.CHAT_ROUTE) {
                        popUpTo(AgriiaDestinations.SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = AgriiaDestinations.CHAT_ROUTE,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            val viewModel: ChatViewModel = viewModel(
                factory = AgriiaViewModelFactory(appContainer)
            )
            ChatScreen(viewModel = viewModel)
        }
    }
}
