package com.dicoding.asclepius.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.AppDatabase
import com.dicoding.asclepius.data.model.PredictionModel
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.label.Category
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private lateinit var classifierHelper: ImageClassifierHelper
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            currentImageUri = result.data?.data
            showImage()
        }
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
                Log.d("response",categories.toString())
                showToast("Analysis complete: ${categories.joinToString(", ") { it.label }}")
                moveToResult(categories)
            }
        } ?: showToast("No image selected.")
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
            intent.putExtra("ImageUri", it) // Directly pass the Uri
        }

        // Assuming predictions are available and you have methods to get readable strings from them
        val predictionsText = predictions.joinToString(", ") { "${it.label}" }
        val confidenceScores = predictions.joinToString(", ") { "${it.score * 100}"}
        intent.putExtra("Timestamp", formattedDateTime) // Pass the timestamp to the ResultActivity
        intent.putExtra("PredictionResults", predictionsText)
        intent.putExtra("ConfidenceScores", confidenceScores.toDouble()) // Adjust this according to actual data availability
        if (predictionsText != null && formattedDateTime != null) {
            val prediction = PredictionModel(timestamp = formattedDateTime, predictionResults = predictionsText, confidenceScores = confidenceScores.toDouble())
            lifecycleScope.launch {
                database.predictionDao().insert(prediction)
            }
        }

        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        classifierHelper.close() // Properly close the classifier when the activity is destroyed
        super.onDestroy()
    }
}
