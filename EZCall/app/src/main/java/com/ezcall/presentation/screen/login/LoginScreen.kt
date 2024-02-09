package com.ezcall.presentation.screen.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.ezcall.R
import com.ezcall.data.dataSource.remote.ApiResponse
import com.ezcall.data.dataSource.remote.entities.LoginResponse
import com.ezcall.presentation.CoroutinesErrorHandler
import com.ezcall.presentation.TokenViewModel
import com.ezcall.presentation.navigation.MainDestinations
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    onNavigateToSubScreen: (String, NavBackStackEntry) -> Unit,
    backStackEntry: NavBackStackEntry,
) {

    val loginResponse by viewModel.loginResponse.observeAsState(initial = ApiResponse.Idle)
    val verifyResponse by viewModel.verifyResponse.observeAsState(initial = ApiResponse.Idle)

    val passwordFocusRequester = remember { FocusRequester() }
    val userNameFocusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }

    var isErrorDisplayed by remember { mutableStateOf(false) }
    var isToastDisplayed by remember { mutableStateOf(false) }

//    TODO fix verify apprently it runs in first run and shows toast and runns in aloop
    val token by tokenViewModel.token.observeAsState()
    LaunchedEffect(key1 = token, block = {
        token?.let {
            viewModel.verifyToken(it, coroutinesErrorHandler = object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    Timber.e("LoginScreen token verify Error")
                }

            })
        }
    })

    when (verifyResponse) {
        is ApiResponse.Success -> onNavigateToSubScreen.invoke(
            MainDestinations.KEYPAD_ROUTE,
            backStackEntry
        )
        else -> {}
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), horizontalAlignment = Alignment.Start
        ) {
//            Image(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(190.dp),
//                painter = painterResource(id = tel.aiotel.core_ui.R.drawable.vector_aiotel_logo),
//                contentDescription = "login logo"
//            )

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 84.dp),
                text = "ارتباط صوتی و تصویری آسان",
                fontSize = 24.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "EZCall ",
                fontSize = 24.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
            )
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(2f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp)
                )
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(top = 16.dp),
            ) {

                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = stringResource(R.string.user_name),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )

                OutlinedTextField(
                    isError = isErrorDisplayed && viewModel.username.isEmpty(),
                    value = viewModel.username,
                    onValueChange = viewModel::onUsernameChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(userNameFocusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray, cursorColor = Color.Gray
                    ),
                    singleLine = true,
                    maxLines = 1,
                    interactionSource = remember { MutableInteractionSource() },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                )

                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = stringResource(R.string.password),
                    modifier = Modifier.padding(
                        start = 8.dp, end = 8.dp, bottom = 8.dp, top = 16.dp
                    )
                )

                OutlinedTextField(isError = isErrorDisplayed && viewModel.password.isEmpty(),
                    value = viewModel.password,
                    onValueChange = viewModel::onPasswordChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Gray,
                        unfocusedBorderColor = Color.LightGray, cursorColor = Color.Gray
                    ),
                    singleLine = true,
                    maxLines = 1,
                    interactionSource = remember { MutableInteractionSource() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.onLoginClicked(object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                Timber.e("onError: $message")
                            }

                        })
                    }),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Lock else Icons.Filled.Lock,
                                contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                            )
                        }
                    })

                Button(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                        .height(51.dp),
                    onClick = {
                        isErrorDisplayed = false
                        viewModel.onLoginClicked(object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                Timber.e("onError: $message")
                            }

                        })
                    },
                ) {
                    when (loginResponse) {
                        is ApiResponse.Success -> {
                            Text(stringResource(R.string.login))
                            LaunchedEffect(
                                key1 = Unit,
                                block = {
                                    tokenViewModel.saveToken((loginResponse as ApiResponse.Success<LoginResponse>).data.accessToken)
                                    tokenViewModel.saveRefreshToken((loginResponse as ApiResponse.Success<LoginResponse>).data.refreshToken)
                                    onNavigateToSubScreen.invoke(
                                        MainDestinations.KEYPAD_ROUTE,
                                        backStackEntry
                                    )

                                })
                        }

                        is ApiResponse.Failure -> {
                            Text(stringResource(R.string.login))
//                            TODO Combine launched effect and if
                            LaunchedEffect(key1 = Unit, block = {
                                isToastDisplayed = true
                            })
                            if (!isToastDisplayed) {
                                Timber.e((loginResponse as ApiResponse.Failure).errorMessage)
                                Toast.makeText(
                                    LocalContext.current,
                                    (loginResponse as ApiResponse.Failure).errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is ApiResponse.Loading -> {
//                            CircularProgressIndicator(
//                                color = Color.White, modifier = Modifier
//                                    .height(32.dp)
//                                    .width(32.dp)
//
//                            )
                        }

                        else -> {
                            Text("ورود")
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))


                OutlinedButton(
                    modifier = Modifier
                        .padding(32.dp)
                        .height(51.dp)
                        .fillMaxWidth(0.6f)
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onNavigateToSubScreen(
                            MainDestinations.SIGN_UP_ROUTE,
                            backStackEntry
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(

                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                ) {

                    Text(text = "ثبت نام")

                }


            }

        }
    }

}