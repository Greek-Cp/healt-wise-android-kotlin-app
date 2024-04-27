package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.data.database.AppDatabase
import com.dicoding.asclepius.data.model.PredictionModel
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.util.ImageUtils
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var classifierHelper: ImageClassifierHelper
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = AppDatabase.getDatabase(this)
        classifierHelper = ImageClassifierHelper(this)

        binding.galleryButton.setOnClickListener {

            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }
        binding.idBackBtnDetail.setOnClickListener { finish() }

    }
    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri = result.data?.data
            imageUri?.let {
                startCrop(it)
            }
        } else {
            resetImageSelection()
        }
    }

    var cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            resetImageSelection()
        }
    }

    private fun resetImageSelection() {
        currentImageUri = null
    }
    fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(applicationContext.cacheDir, "cropped_${UUID.randomUUID()}.jpg"))
        val options = UCrop.Options().apply {
            setCompressionQuality(100)
        }
        val intent = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1080, 1080)
            .getIntent(applicationContext)
        cropImageLauncher.launch(intent)
    }



    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri ->
            val categories = classifierHelper.classifyStaticImage(uri)
            if (categories.isNullOrEmpty()) {
                showToast("No results found or error in analysis.")
            } else {
                showToast("Analisis Selesai: ${categories.joinToString(", ") { it.label }}")
                moveToResult(categories)
            }
        } ?: showToast("Tidak ada gambar dipilih")
    }

    private fun moveToResult(predictions: List<Category>) {
        // TODO: Navigate to result screen if analysis is successful.
        val formattedDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            currentDateTime.format(formatter)
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            dateFormat.format(Date())
        }
        val intent = Intent(this, ResultActivity::class.java)
        currentImageUri?.let {
            intent.putExtra("ImageUri", it)
        }
        val bitmapImage = ImageUtils.uriToBitmap(this,currentImageUri!!);
        val imageBytes = ImageUtils.bitmapToByteArray(bitmapImage!!);
        val predictionsText = predictions.joinToString(", ") { "${it.label}" }
        val confidenceScores = predictions.joinToString(", ") { "${it.score * 100}"}
        intent.putExtra("Timestamp", formattedDateTime)
        intent.putExtra("PredictionResults", predictionsText)
        intent.putExtra("ConfidenceScores", confidenceScores.toDouble())
        intent.putExtra("fromPage","PageAnalyze")

        if (predictionsText != null && formattedDateTime != null) {
            val prediction = PredictionModel(timestamp = formattedDateTime, predictionResults = predictionsText, confidenceScores = confidenceScores.toDouble(), imagePrediction = imageBytes)
            lifecycleScope.launch {
                database.predictionDao().insert(prediction)
            }
        }
        startActivity(intent)
        finish()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        classifierHelper.close()
        super.onDestroy()
    }
}
