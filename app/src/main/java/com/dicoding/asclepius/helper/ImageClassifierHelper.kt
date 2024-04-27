package com.dicoding.asclepius.helper

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import android.content.Context
import android.os.Build
import android.provider.MediaStore


class ImageClassifierHelper(private val context: Context) { // Context passed through the constructor

    private var model: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(0.5f) // Set the threshold for probability.
            .setMaxResults(2) // Set the maximum results.
            .build()
        model = ImageClassifier.createFromFileAndOptions(
            context,
            "cancer_classification.tflite", // Ensure this path is correct
            options
        )
    }

    fun classifyStaticImage(imageUri: Uri): MutableList<Category>? {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, info, source ->
                // Konversi gambar menjadi format ARGB_8888
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
                decoder.setTargetSampleSize(1) // Atur faktor skala jika diperlukan
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        // Preprocess the image and convert it to TensorImage.
        val tensorImage = TensorImage.fromBitmap(bitmap)

        // Run model inference and get the result.
        val outputs = model?.classify(tensorImage)

        // Return the list of probability categories.
        return outputs?.get(0)?.categories
    }


    fun close() {
        model?.close()
    }
}
