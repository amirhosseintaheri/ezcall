package com.ezcall.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ezcall.data.dataSource.webRTC.SignalingClient
import com.ezcall.data.dataSource.webRTC.peer.StreamPeerConnectionFactory
import com.ezcall.data.dataSource.webRTC.sessions.LocalWebRtcSessionManager
import com.ezcall.data.dataSource.webRTC.sessions.WebRtcSessionManager
import com.ezcall.data.dataSource.webRTC.sessions.WebRtcSessionManagerImpl
import com.ezcall.presentation.navigation.MainDestinations
import com.ezcall.presentation.navigation.rememberEZCallNavController
import com.ezcall.presentation.screen.drawer.DrawerScreen
import com.ezcall.presentation.screen.inComingCall.IncomingCallScreen
import com.ezcall.presentation.screen.keypad.KeypadScreen
import com.ezcall.presentation.screen.login.LoginScreen
import com.ezcall.presentation.screen.signup.SignUpScreen
import com.ezcall.presentation.screen.video.VideoCallScreen
import com.ezcall.presentation.ui.theme.EZCallTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)

        val sessionManager: WebRtcSessionManager = WebRtcSessionManagerImpl(
            context = this,
            signalingClient = SignalingClient(),
            peerConnectionFactory = StreamPeerConnectionFactory(this)
        )
        setContent {
            EZCallTheme {
                CompositionLocalProvider(LocalWebRtcSessionManager provides sessionManager) {
                    val state by sessionManager.signalingClient.sessionStateFlow.collectAsState()

                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        EZCallApp()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EZCallApp() {
    val ezCallNavController = rememberEZCallNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isDrawerGestureEnabled by remember { mutableStateOf(false) }



    ModalNavigationDrawer(drawerContent = {
        DrawerScreen(drawerState = drawerState,
            onUpPressed = { coroutineScope.launch { drawerState.close() } })

    }, content = {
        NavHost(
            navController = ezCallNavController.navController,
            startDestination = MainDestinations.LOGIN_ROUTE
        ) {
            ezCallNavGraph(upPress = ezCallNavController::upPress,

                onNavigateToBottomBarRoute = { route ->
                    isDrawerGestureEnabled =
                        true // Re-enable drawer gestures when navigating
                    ezCallNavController.navigateToBottomBarRoute(route)
                },
                onNavigateToAnySubScreen = { route, backStackEntry ->
                    isDrawerGestureEnabled =
                        false // Disable drawer gestures when navigating
                    ezCallNavController.navigateToAnyScreen(
                        route, backStackEntry
                    )
                },
                drawerState = drawerState,
                onNavigateWithParam = { route, id, backStackEntry ->
                    isDrawerGestureEnabled =
                        false // Disable drawer gestures when navigating
                    ezCallNavController.navigateWithParam(
                        route = route, param = id, from = backStackEntry
                    )
                })
        }

    })
}

@OptIn(ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.ezCallNavGraph(
    upPress: () -> Unit,
    onNavigateToBottomBarRoute: (String) -> Unit,
    onNavigateToAnySubScreen: (String, NavBackStackEntry) -> Unit,
    drawerState: DrawerState,
    onNavigateWithParam: (String, Long, NavBackStackEntry) -> Unit,
) {

    composable(
        route = MainDestinations.LOGIN_ROUTE,
    ) { backStackEntry ->
        LoginScreen(
            onNavigateToSubScreen = onNavigateToAnySubScreen,
            backStackEntry = backStackEntry
        )
    }

    composable(route = MainDestinations.SIGN_UP_ROUTE) { backStackEntry ->
        SignUpScreen(onSignUpSuccess = onNavigateToAnySubScreen, backStackEntry = backStackEntry)
    }

    composable(route = MainDestinations.KEYPAD_ROUTE) {
        KeypadScreen(
            onNavigateToSubScreen = onNavigateToAnySubScreen,
            backStackEntry = it
        )
    }
    composable(route = MainDestinations.IN_CALL_SCREEN) {
        VideoCallScreen(
            onNavigateToSubScreen = onNavigateToAnySubScreen,
            backStackEntry = it
        )
    }
    composable(route = MainDestinations.INCOMING_CALL_SCREEN) {
        IncomingCallScreen(
            onNavigateToSubScreen = onNavigateToAnySubScreen,
            backStackEntry = it
        )
    }
}
