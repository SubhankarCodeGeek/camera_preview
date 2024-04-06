package com.plcoding.cameraxguide.utils

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.widget.Toast
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider

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