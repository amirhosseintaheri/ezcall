package com.ezcall.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Destinations used in the [EZCall App].
 */
object MainDestinations {
    const val KEYPAD_ROUTE = "dialer"
    const val IN_CALL_SCREEN = "in_call"
    const val INCOMING_CALL_SCREEN = "incoming_call"
    const val HOME_ROUTE = "home"
    const val SETTINGS_ROUTE = "settings"
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signup"
    const val CONTACTS_ROUTE = "contacts"
    const val CALL_HISTORY = "call_history"
}

/**
 * Remembers and creates an instance of [EZCallNavController]
 */
@Composable
fun rememberEZCallNavController(
    navController: NavHostController = rememberNavController(),
): EZCallNavController = remember(navController) {
    EZCallNavController(navController)
}

/**
 * Responsible for holding UI Navigation logic.
 */
@Stable
class EZCallNavController(
    val navController: NavHostController,
) {

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {

        if (route != currentRoute) {

            navController.navigate(route) {
                anim {
                    enter = 0
                    exit = 0
                    popEnter = 0
                    popExit = 0
                }
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToAnyScreen(route: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(route)
        }
    }

    fun navigateWithParam(param: Long, from: NavBackStackEntry, route: String) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("$route/$param")
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}