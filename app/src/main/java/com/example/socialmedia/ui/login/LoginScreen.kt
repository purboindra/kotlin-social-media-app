package com.example.socialmedia.ui.login

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.socialmedia.R
import com.example.socialmedia.data.model.State
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.AppOutlinedTextField
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.example.socialmedia.viewmodel.AuthViewModel
import com.example.socialmedia.viewmodel.ObsecurePasswordType
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    
    val loginState by authViewModel.loginState.collectAsState()
    
    val emailText by authViewModel.emailText.collectAsState()
    val passwordText by authViewModel.passwordText.collectAsState()
    val hasObsecurePassword by authViewModel.hasObsecurePassword.collectAsState()
    
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    val coroutineScope = rememberCoroutineScope()
    
    val annotedString = buildAnnotatedString {
        append("Don't have an account? ")
        withStyle(
            style = SpanStyle(
                color = BluePrimary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up")
            append("Register")
            pop()
        }
    }
    
    LaunchedEffect(loginState) {
        when (loginState) {
            is State.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Successfully registered",
                        duration = SnackbarDuration.Long,
                    )
                }
            }
            
            is State.Failure -> {
                val message =
                    (loginState as State.Failure).throwable.message
                        ?: "Something went wrong"
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Long,
                    )
                }
            }
            
            else -> {}
        }
    }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier
            .statusBarsPadding()
            .safeContentPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    placeholderText = "Email",
                    onValueChange = {
                        authViewModel.onChangeEmailText(it)
                    }
                )
                8.VerticalSpacer()
                AppOutlinedTextField(
                    query = passwordText,
                    onValueChange = {
                        authViewModel.onChangePasswordText(it)
                    },
                    placeholderText = "Password",
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
                10.VerticalSpacer()
                AppElevatedButton(
                    onClick = {
                        authViewModel.login()
                    },
                    enabled = loginState !is State.Loading,
                    text = if (loginState is State.Loading) "Loading..." else "Log In",
                    modifier = Modifier.fillMaxWidth()
                )
                
                5.VerticalSpacer()
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        thickness = 2.dp
                    )
                    8.HorizontalSpacer()
                    
                    Text(
                        text = "Or",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            GrayDark,
                        )
                    )
                    8.HorizontalSpacer()
                    
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        thickness = 2.dp
                    )
                }
                
                5.VerticalSpacer()
                
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    border = BorderStroke(1.dp, BluePrimary),
                    onClick = {},
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SvgImage(
                            svgResource = R.raw.google_icon,
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(32.dp)
                        )
                        5.HorizontalSpacer()
                        Text(
                            "Log In With Google",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BluePrimary,
                            )
                        )
                    }
                }
                
                5.VerticalSpacer()
                BasicText(
                    annotedString,
                    modifier = Modifier.clickable {
                        navHostController.navigate(Screens.Register.route)
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = GrayDark
                    )
                )
            }
        }
    }
}