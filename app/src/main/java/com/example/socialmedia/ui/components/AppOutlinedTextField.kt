package com.example.socialmedia.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
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
    roundedCornerShape: Dp = 12.dp,
    colors: TextFieldColors? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String = "Your placeholder",
    visualTransformation: VisualTransformation? = null,
    isError: Boolean? = null,
    validator: ((String) -> String?)? = null,
    shape: Shape? = null,
    maxLines: Int = 1,
    singleLine:Boolean = true,
) {
    
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    Column {
        OutlinedTextField(
            value = query,
            onValueChange = {
                onValueChange(it)
                errorMessage = validator?.invoke(it)
            },
            maxLines = maxLines,
            modifier = modifier ?: Modifier.fillMaxWidth(),
            shape = shape ?: RoundedCornerShape(roundedCornerShape),
            colors = colors ?: OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = GrayPrimary,
            ),
            singleLine = singleLine,
            placeholder = placeholder ?: {
                Text(
                    placeholderText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = GrayPrimary,
                    )
                )
            },
            visualTransformation = visualTransformation
                ?: VisualTransformation.None,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            isError = isError ?: false
        )
        
        errorMessage?.let {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
        
    }
}