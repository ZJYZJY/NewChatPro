package com.fzm.newchatpro.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.fzm.newchatpro.ui.theme.ChatColor
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation

internal sealed class Screen(val route: String) {
    object Session : Screen("session")
    object AddressBook : Screen("address_book")
    object Wallet : Screen("wallet")
    object Mine : Screen("mine")
}

private sealed class LeafScreen(
    private val route: String
) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Session : LeafScreen("session")
    object AddressBook : LeafScreen("address_book")
    object Wallet : LeafScreen("wallet")
    object Mine : LeafScreen("mine")

}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Session.route,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() },
        modifier = modifier
    ) {
        addSessionTopLevel(navController, onOpenSettings)
        addAddressBookTopLevel(navController, onOpenSettings)
        addWalletTopLevel(navController, onOpenSettings)
        addMineTopLevel(navController, onOpenSettings)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSessionTopLevel(
    navController: NavController,
    openSettings: () -> Unit
) {
    navigation(
        route = Screen.Session.route,
        startDestination = LeafScreen.Session.createRoute(Screen.Session)
    ) {
        addSession(navController, Screen.Session)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addAddressBookTopLevel(
    navController: NavController,
    openSettings: () -> Unit
) {
    navigation(
        route = Screen.AddressBook.route,
        startDestination = LeafScreen.AddressBook.createRoute(Screen.AddressBook)
    ) {
        addAddressBook(navController, Screen.AddressBook)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWalletTopLevel(
    navController: NavController,
    openSettings: () -> Unit
) {
    navigation(
        route = Screen.Wallet.route,
        startDestination = LeafScreen.Wallet.createRoute(Screen.Wallet)
    ) {
        addWallet(navController, Screen.Wallet)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMineTopLevel(
    navController: NavController,
    openSettings: () -> Unit
) {
    navigation(
        route = Screen.Mine.route,
        startDestination = LeafScreen.Mine.createRoute(Screen.Mine)
    ) {
        addMine(navController, Screen.Mine)
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addSession(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.Session.createRoute(root),
        debugLabel = "Session()"
    ) {
        EmptyTab("Session")
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addAddressBook(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.AddressBook.createRoute(root),
        debugLabel = "AddressBook()"
    ) {
        EmptyTab("AddressBook")
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addWallet(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.Wallet.createRoute(root),
        debugLabel = "Wallet()"
    ) {
        EmptyTab("Wallet")
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.addMine(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.Mine.createRoute(root),
        debugLabel = "Mine()"
    ) {
        EmptyTab("Mine")
    }
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}

@Composable
fun EmptyTab(text: String, color: Color = Color(0xFF8BC34A)) {
    Box(
        modifier = Modifier.fillMaxSize().background(color)
    ) {
        Text(
            text = text,
            color = Color(0xFF242424),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
