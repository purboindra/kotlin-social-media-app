package com.example.socialmedia.ui.components

import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.example.socialmedia.data.model.SnackbarConfig

@Composable
fun AppSnackbar(
    snackbarHostState: SnackbarHostState,
    snackbarConfig: SnackbarConfig?,
) {
    SnackbarHost(
        hostState = snackbarHostState,
    ) { data ->
        snackbarConfig?.let {
            Snackbar(
                snackbarData = data,
                contentColor = it.contentColor,
                containerColor = it.backgroundColor,
            )
        }
    }
}