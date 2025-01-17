package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.socialmedia.ui.theme.BluePrimary
import com.example.socialmedia.ui.theme.GrayPrimary

@Composable
fun AppOutlinedTextField(
    query: String,
    onValueChange: (query: String) -> Unit,
    modifier: Modifier? = null,
    roundedCornerShape: Dp? = null,
    colors: TextFieldColors? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String? = null,
    visualTransformation: VisualTransformation? = null,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onValueChange,
        modifier = modifier ?: Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(roundedCornerShape ?: 12.dp),
        colors = colors ?: OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BluePrimary,
            unfocusedBorderColor = GrayPrimary,
        ),
        placeholder = placeholder ?: {
            Text(
                placeholderText ?: "Your placeholder",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = GrayPrimary,
                )
            )
        },
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon
    )
}