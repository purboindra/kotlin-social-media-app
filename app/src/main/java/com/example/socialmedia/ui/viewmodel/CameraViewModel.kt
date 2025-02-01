package com.example.socialmedia.ui.viewmodel

import android.content.Context
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
    
    private var surfaceMateringPointFactory: SurfaceOrientedMeteringPointFactory? =
        null
    private var cameraControl: CameraControl? = null
    
    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    
    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMateringPointFactory = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat()
            )
        }
    }
    
    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(appContext)
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase
        )
        
        cameraControl = camera.cameraControl
        
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
            cameraControl = null
        }
    }
    
    suspend fun switchCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(appContext)
        
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
        processCameraProvider.unbindAll()
        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase
        )
        
    }
    
    fun tapToFocus(tapCoords: Offset) {
        val point =
            surfaceMateringPointFactory?.createPoint(tapCoords.x, tapCoords.y)
        if (point != null) {
            val meteringAction = FocusMeteringAction.Builder(point).build()
            cameraControl?.startFocusAndMetering(meteringAction)
        }
    }
}