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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.socialmedia.ui.components.AppElevatedButton
import com.example.socialmedia.ui.components.SvgImage
import com.example.socialmedia.ui.navigation.Screens
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayDark
import com.example.socialmedia.ui.theme.GrayPrimary
import com.example.socialmedia.utils.HorizontalSpacer
import com.example.socialmedia.utils.VerticalSpacer
import com.example.socialmedia.viewmodel.AuthViewModel
import com.example.socialmedia.viewmodel.ObsecurePasswordType

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    
    val context = LocalContext.current
    
    val emailText by authViewModel.emailText.collectAsState()
    val passwordText by authViewModel.passwordText.collectAsState()
    val hasObsecurePassword by authViewModel.hasObsecurePassword.collectAsState()
    
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    
    val annotedString = buildAnnotatedString {
        append("Don't have an account? ")
        withStyle(
            style = SpanStyle(color = BluePrimary, fontWeight = FontWeight.SemiBold)
        ) {
            pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up")
            append("Register")
            pop()
        }
    }
    
    Scaffold(
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
                    svgResource = R.raw.instagram_logo,
                    contentDescription = "Instagram Logo",
                    modifier = Modifier.size(48.dp)
                )
                10.VerticalSpacer()
                OutlinedTextField(
                    value = emailText,
                    onValueChange = {
                        authViewModel.onChangeEmailText(it)
                    },
                    modifier = Modifier.fillParentMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = GrayPrimary,
                    ),
                    placeholder = {
                        Text(
                            "Username or email",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = GrayPrimary,
                            )
                        )
                    },
                )
                8.VerticalSpacer()
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = {
                        authViewModel.onChangePasswordText(it)
                    },
                    modifier = Modifier.fillParentMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        unfocusedBorderColor = GrayPrimary,
                    ),
                    visualTransformation = if (!hasObsecurePassword) VisualTransformation.None else
                        PasswordVisualTransformation(),
                    placeholder = {
                        Text(
                            "Password",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = GrayPrimary,
                            )
                        )
                    },
                    trailingIcon = {
                        Icon(
                            if (!hasObsecurePassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "Visibility",
                            modifier = Modifier.clickable {
                                authViewModel.toggleObsecure(ObsecurePasswordType.Password)
                            }
                        )
                    }
                )
                10.VerticalSpacer()
                AppElevatedButton(
                    onClick = {},
                    text = "Log In",
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
                            "Log In With Google", style = MaterialTheme.typography.labelSmall.copy(
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