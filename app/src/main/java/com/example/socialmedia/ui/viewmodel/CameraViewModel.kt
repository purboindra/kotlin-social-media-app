package com.example.socialmedia.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.socialmedia.utils.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
    
    private var surfaceMateringPointFactory: SurfaceOrientedMeteringPointFactory? =
        null
    private var cameraControl: CameraControl? = null
    
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    
    var imageCapture: ImageCapture? = null
    
    private val _recordedVideoUri = MutableStateFlow<Uri?>(null)
    val recordedVideoUri: StateFlow<Uri?> = _recordedVideoUri
    
    private val _videoDuration = MutableStateFlow<Long>(0)
    val videoDuration: StateFlow<Long> = _videoDuration
    
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    
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
    ): Nothing = withContext(Dispatchers.Main) {
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(appContext)
        
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetRotation(Surface.ROTATION_0)
            .setFlashMode(ImageCapture.FLASH_MODE_ON)
            .build()
        
        // INITIALIZE CAMERA FOR VIDEO
        val recorder = Recorder.Builder().setQualitySelector(
            QualitySelector.from(Quality.HD)
        ).build()
        
        videoCapture = VideoCapture.withOutput(recorder)
        
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase,
            imageCapture,
            videoCapture
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
    ) = withContext(Dispatchers.Main) {
        val processCameraProvider =
            ProcessCameraProvider.awaitInstance(appContext)
        
        cameraSelector =
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
        
        processCameraProvider.unbindAll()
        
        val newPreview = Preview.Builder().build().apply {
            setSurfaceProvider { newSurfaceRequest ->
                _surfaceRequest.update { newSurfaceRequest }
            }
        }
        
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(Surface.ROTATION_0)
            .build()
        
        val camera = processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            newPreview,
            imageCapture
        )
        
        cameraControl = camera.cameraControl
    }
    
    fun captureVideo(context: Context) {
        recording = FileHelper.captureVideo(
            onVideoSaved = { uri ->
                _recordedVideoUri.update { uri }
            },
            onDurationUpdate = { duration ->
                _videoDuration.value = duration
            },
            context,
            videoCapture,
            recording,
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