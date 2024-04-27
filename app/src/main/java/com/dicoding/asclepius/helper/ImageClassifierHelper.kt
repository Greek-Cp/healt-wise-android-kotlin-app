package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.dicoding.asclepius.BuildConfig
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class ImageClassifierHelper(private val context: Context) {
    private var model: ImageClassifier? = null
    init {
        setupImageClassifier()
    }
    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(0.5f)
            .setMaxResults(1)
            .build()
        model = ImageClassifier.createFromFileAndOptions(
            context,
            BuildConfig.MODEL_NAME,
            options
        )
    }
    fun classifyStaticImage(imageUri: Uri): MutableList<Category>? {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, info, source ->
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE)
                decoder.setTargetSampleSize(1)
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }
        val tensorImage = TensorImage.fromBitmap(bitmap)
        val outputs = model?.classify(tensorImage)
        return outputs?.get(0)?.categories
    }
    fun close() {
        model?.close()
    }
}
