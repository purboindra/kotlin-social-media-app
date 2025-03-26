package com.example.socialmedia.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.ZoomState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.utils.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _isFlashOn = MutableStateFlow(false)
    val isFlashOn: StateFlow<Boolean> = _isFlashOn

    private val _hasFlashUnit = MutableStateFlow(false)
    val hasFlashUnit: StateFlow<Boolean> = _hasFlashUnit

    private val _isLoadingBindCamera = MutableStateFlow(false)
    val isLoadingBindCamera: StateFlow<Boolean> = _isLoadingBindCamera

    private var surfaceMateringPointFactory: SurfaceOrientedMeteringPointFactory? =
        null
    private var cameraControl: CameraControl? = null
    private var cameraControlInstaStory: CameraControl? = null

    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private var _processCameraProvider: ProcessCameraProvider? = null
    val processCameraProvider: ProcessCameraProvider? = _processCameraProvider

    var imageCapture: ImageCapture? = null

    private val _recordedVideoUri = MutableStateFlow<Uri?>(null)
    val recordedVideoUri: StateFlow<Uri?> = _recordedVideoUri

    private val _videoDuration = MutableStateFlow<Long>(0)
    val videoDuration: StateFlow<Long> = _videoDuration

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private val _minZoom = MutableStateFlow(1f)
    val minZoom = _minZoom.asStateFlow()

    private val _maxZoom = MutableStateFlow(5f)
    val maxZoom = _maxZoom.asStateFlow()

    private val _restartTrigger = MutableStateFlow(false)

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
            surfaceMateringPointFactory = SurfaceOrientedMeteringPointFactory(
                newSurfaceRequest.resolution.width.toFloat(),
                newSurfaceRequest.resolution.height.toFloat()
            )
        }
    }


    suspend fun restartCamera(
        context: Context, lifecycleOwner: LifecycleOwner
    ) = withContext(Dispatchers.Main) {
        _processCameraProvider =
            ProcessCameraProvider.awaitInstance(context)
        _processCameraProvider?.unbindAll()

        val newPreview = Preview.Builder().build().apply {
            setSurfaceProvider { newSurfaceRequest ->
                _surfaceRequest.update { newSurfaceRequest }
            }
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(Surface.ROTATION_0)
            .build()

        val camera = _processCameraProvider?.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            newPreview,
            imageCapture
        )



        cameraControl = camera?.cameraControl
    }

    val restartFlow = _restartTrigger.asStateFlow()

    fun toggleIsRecording() {
        _isRecording.value = !_isRecording.value
    }

    private fun initZoom(camera: Camera) {
        val zoomState = camera.cameraInfo.zoomState.value
        _minZoom.value = zoomState?.minZoomRatio ?: 1f
        _maxZoom.value = zoomState?.maxZoomRatio ?: 5f
    }

    @SuppressLint("RestrictedApi")
    fun setZoom(value: Float) {

        val debugValue = mapOf(
            "minZoom" to _minZoom.value,
            "maxZoom" to _maxZoom.value,
            "value" to value,
            "cameraControlNotNull" to (cameraControl != null).toString()
        )

        Log.d("Cameraviewmodel", "Debug set zoom: $debugValue")

        cameraControl?.setLinearZoom(value)
    }

    fun toggleFlashLight() {
        _isFlashOn.value = !_isFlashOn.value
        cameraControl?.enableTorch(
            isFlashOn.value
        )
    }

    fun stopRecordingInstaStory() {
        viewModelScope.launch {
            Log.d("CameraViewModel", "Stopping recording...")
            cancel()
            _isRecording.value = false
            recording?.stop()
            recording = null
        }
    }

    fun startRecordingInstaStory(context: Context) {
        videoCapture?.let { vc ->
            viewModelScope.launch {
                recording = FileHelper.startVideoRecordingInstaStory(
                    context,
                    videoCapture = vc,
                    onSave = { uri ->
                        Log.d(
                            "CameraViewModel",
                            "Saved uri: $uri"
                        )
                        _recordedVideoUri.value = uri
                    },
                    onError = { error ->
                        Log.e(
                            "CameraViewModel",
                            "Error recording: ${error.message}"
                        )
                    },
                    onRecordingStart = {
                        _isRecording.value = true
                    },
                    onRecordingStop = {
                        _isRecording.value = false
                    },
                    onDurationUpdate = { duration ->
                        _videoDuration.value = duration
                    }
                )
            }
        } ?: Log.e("CameraViewModel", "Video capture is not bound yet")
    }

    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner
    ): Nothing = withContext(Dispatchers.Main) {
        _isLoadingBindCamera.value = true
        _processCameraProvider =
            ProcessCameraProvider.awaitInstance(appContext)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setTargetRotation(Surface.ROTATION_0)
            .setFlashMode(ImageCapture.FLASH_MODE_ON)
            .build()

        val camera = _processCameraProvider?.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase,
            imageCapture,
        )

        cameraControl = camera?.cameraControl

        camera?.let { initZoom(it) }

        _hasFlashUnit.value = camera?.cameraInfo?.hasFlashUnit() ?: false

        _isLoadingBindCamera.value = false
        try {
            awaitCancellation()
        } finally {
            _processCameraProvider?.unbindAll()
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
        camera?.let { initZoom(it) }
    }

    fun stopRecording() {
        recording?.stop()
        recording = null
    }

    fun captureVideo(context: Context, onVideoSaved: (Uri) -> Unit) {
        recording = FileHelper.captureVideo(
            onVideoSaved = onVideoSaved,
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

    fun resetVideoDuration() {
        _videoDuration.value = 0
    }
}