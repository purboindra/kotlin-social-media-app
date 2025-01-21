package com.example.socialmedia.ui.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.components.AppSnackbar
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.example.socialmedia.ui.viewmodel.AuthViewModel
import com.example.socialmedia.ui.viewmodel.ObsecurePasswordType
import com.example.socialmedia.ui.viewmodel.SnackbarViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    snackbarViewModel: SnackbarViewModel = hiltViewModel()
) {
    
    val coroutineScope = rememberCoroutineScope()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarConfig by snackbarViewModel.snackbarState.collectAsState()
    
    val registerState by authViewModel.registerState.collectAsState()
    
    val emailText by authViewModel.emailText.collectAsState()
    val passwordText by authViewModel.passwordText.collectAsState()
    val confirmPasswordText by authViewModel.confirmPasswordText.collectAsState()
    val usernameText by authViewModel.usernameText.collectAsState()
    
    val hasObsecurePassword by authViewModel.hasObsecurePassword.collectAsState()
    val hasObsecureConfirmPassword by authViewModel.hasObsecureConfirmPassword.collectAsState()
    
    val annotatedString = buildAnnotatedString {
        append("Already have an account? ")
        withStyle(
            style = SpanStyle(
                color = BluePrimary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            pushStringAnnotation(tag = "SIGN_IN", annotation = "sign_in")
            append("Login")
            pop()
        }
    }
    
    LaunchedEffect(registerState) {
        when (registerState) {
            is State.Success -> {
                coroutineScope.launch {
                    snackbarViewModel.showSnackbar(
                        "Successfully logged in",
                        isError = true
                    )
                }
            }
            
            is State.Failure -> {
                val message =
                    (registerState as State.Failure).throwable.message
                        ?: "Something went wrong"
                coroutineScope.launch {
                    snackbarViewModel.showSnackbar(message, isError = false)
                }
            }
            
            else -> {}
        }
    }
    
    LaunchedEffect(snackbarConfig) {
        snackbarConfig?.let {
            snackbarHostState.showSnackbar(it.message, duration = it.duration)
        }
    }
    
    Scaffold(
        snackbarHost = { AppSnackbar(snackbarHostState, snackbarConfig) },
        modifier = Modifier
            .statusBarsPadding()
            .safeContentPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            item {
                SvgImage(
                    svgResource = R.raw.instagram_horizontal_logo,
                    contentDescription = "Instagram Logo",
                    modifier = Modifier.height(72.dp)
                )
                10.VerticalSpacer()
                AppOutlinedTextField(
                    query = emailText,
                    onValueChange = {
                        authViewModel.onChangeEmailText(it)
                    },
                    placeholderText = "Email",
                    validator = {
                        if (it.isEmpty()) "Email is required"
                        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it)
                                .matches()
                        ) "Invalid email"
                        else null
                    }
                )
                8.VerticalSpacer()
                AppOutlinedTextField(
                    query = usernameText,
                    onValueChange = {
                        authViewModel.onChangeUsernameText(it)
                    },
                    placeholderText = "Username",
                )
                8.VerticalSpacer()
                AppOutlinedTextField(
                    query = passwordText,
                    onValueChange = {
                        authViewModel.onChangePasswordText(it)
                    },
                    placeholderText = "Password",
                    validator = {
                        if (it.isEmpty()) "Password is required"
                        else if (it.length < 6) "Password must be at least 6 characters"
                        else null
                    },
                    visualTransformation = if (!hasObsecurePassword) VisualTransformation.None else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            if (!hasObsecurePassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "Visibility",
                            modifier = Modifier.clickable {
                                authViewModel.toggleObsecure(
                                    ObsecurePasswordType.Password
                                )
                            }
                        )
                    }
                )
                8.VerticalSpacer()
                AppOutlinedTextField(
                    query = confirmPasswordText,
                    onValueChange = {
                        authViewModel.onChangeConfirmPasswordText(it)
                    },
                    placeholderText = "Confirm Password",
                    validator = {
                        if (it.isEmpty()) "Confirm Password is required"
                        else if (it != passwordText) "Password does not match"
                        else null
                    },
                    visualTransformation = if (!hasObsecureConfirmPassword) VisualTransformation.None else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            if (!hasObsecureConfirmPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "Visibility",
                            modifier = Modifier.clickable {
                                authViewModel.toggleObsecure(
                                    ObsecurePasswordType.ConfirmPassword
                                )
                            }
                        )
                    }
                )
                10.VerticalSpacer()
                AppElevatedButton(
                    onClick = {
                        if (!authViewModel.validateRegisterInput()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fill out all fields")
                            }
                            return@AppElevatedButton
                            
                        }
                        authViewModel.register()
                    },
                    enabled = registerState != State.Loading,
                    text = if (registerState is State.Loading) "Loading..." else
                        "Register",
                    modifier = Modifier.fillMaxWidth()
                )
                
                5.VerticalSpacer()
                BasicText(
                    annotatedString,
                    modifier = Modifier.clickable {
                        navHostController.navigate(Screens.Login.route)
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = GrayDark
                    )
                )
            }
        }
    }
    
}