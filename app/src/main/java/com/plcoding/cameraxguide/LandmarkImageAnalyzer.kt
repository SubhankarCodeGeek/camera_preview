package com.plcoding.cameraxguide

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.plcoding.cameraxguide.domain.Classification
import com.plcoding.cameraxguide.domain.LandmarkClassifier
import com.plcoding.cameraxguide.utils.centerCrop

class LandmarkImageAnalyzer(
    private val classifier: LandmarkClassifier,
    private val onResult: (List<Classification>) -> Unit
) : ImageAnalysis.Analyzer {

    private var skipFrameCount = 0
    override fun analyze(image: ImageProxy) {
        if (skipFrameCount % 60 == 0) {
            val rotation = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321)

            val result = classifier.classify(bitmap, rotation)
            onResult(result)
        }
        skipFrameCount++

        image.close()
    }
}