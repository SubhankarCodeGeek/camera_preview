package com.plcoding.cameraxguide.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.util.Log
import android.widget.Toast
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import java.io.File

fun Context.makeToast(msg: String?) = Toast.makeText(
    applicationContext,
    msg,
    Toast.LENGTH_LONG
).show()

@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
fun isBackCameraLevel3Device(cameraProvider: ProcessCameraProvider): Boolean {
    return CameraSelector.DEFAULT_BACK_CAMERA
        .filter(cameraProvider.availableCameraInfos)
        .firstOrNull()
        ?.let { Camera2CameraInfo.from(it) }
        ?.getCameraCharacteristic(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) ==
            CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
}

@SuppressLint("MissingPermission")
fun LifecycleCameraController.toggleVideoRecording(
    context: Context,
    outputDirectory: File,
    hasRequiredPermissions: () -> Boolean,
    onVideoSaved: (File) -> Unit,
    onError: (String) -> Unit
) {
    var recording: Recording? = null

    if (recording != null) {
        recording?.stop()
        recording = null
    } else {
        if (!hasRequiredPermissions()) {
            onError("Required permissions are missing.")
            return
        }

        val outputFile = File(outputDirectory, "my_recording.mp4")
        val fileOutputOptions = FileOutputOptions.Builder(outputFile).build()

        recording = this.startRecording(
            fileOutputOptions,
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context)
        ) { event ->
            when (event) {
                is VideoRecordEvent.Finalize -> {
                    if (event.hasError()) {
                        recording?.close()
                        recording = null
                        onError("Video capture failed: ${event.error}")
                    } else {
                        onVideoSaved(outputFile)
                    }
                }
            }
        }
    }
}

fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                image.use { image ->
                    val rotationDegrees = image.imageInfo.rotationDegrees.toFloat()
                    val matrix = Matrix().apply {
                        postRotate(rotationDegrees)
                        // Uncomment the following line if you need to mirror the image
                        // postScale(-1f, 1f)
                    }
                    val originalBitmap = image.toBitmap()
                    val rotatedBitmap = Bitmap.createBitmap(
                        originalBitmap,
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    onPhotoTaken(rotatedBitmap)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}