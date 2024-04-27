package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.AppDatabase
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.home.adapter.AdapterBeritaKesehatan
import com.dicoding.asclepius.view.home.viewmodel.HealthNewsViewModel
import com.dicoding.asclepius.view.util.ImageUtils

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 34 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 34 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: AppDatabase
    private lateinit var viewModel: HealthNewsViewModel
    private lateinit var adapter: AdapterBeritaKesehatan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)

        setContentView(binding.root)
        database = AppDatabase.getDatabase(this)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        displayImageAndResults()

        binding!!.idRecRefferensi.layoutManager = LinearLayoutManager(applicationContext)
        adapter = AdapterBeritaKesehatan(emptyList(), onItemClick = this::onItemClick)
        binding!!.idRecRefferensi.adapter = adapter
        viewModel.articles.observe(this, Observer { articles ->
            Toast.makeText(this, "Data loaded: ${articles.size}", Toast.LENGTH_SHORT).show()
            adapter.updateData(articles) // Pastikan adapter Anda memiliki metode untuk mengupdate datanya
        })
        viewModel.fetchArticles()

    }
    private fun onItemClick(s: String) {

    }


    private fun displayImageAndResults() {
        // Retrieve the URI and display it in the ImageView
        val imageUri: Uri? = intent.parcelable("ImageUri")
        val fromPage: String? = intent.getStringExtra("fromPage")?.toString();
        val predictionId = intent.getIntExtra("prediction_id", 0)
        if(fromPage == "PageAnalyze"){

                imageUri?.let {
                    binding.resultImage.setImageURI(it)
                } ?: Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show()

                // Retrieve the prediction results and confidence scores
                val predictions: String? = intent.getStringExtra("PredictionResults")
                val confidence: Double? = intent.getDoubleExtra("ConfidenceScores",0.0);
                // Update the TextView with the prediction results and confidence scores
                binding.resultText.text = "Results: $predictions\nConfidence: ${confidence.toString()}"
                setCardColorBasedOnPrediction(predictions)
                setExplanationBasedOnConfidence(confidence!!,predictions)


        } else{
            database.predictionDao().getPredictionById(predictionId).observe(this, { prediction ->
                // Update other UI elements as needed
                    val bitmapImage = ImageUtils.byteArrayToBitmap(prediction.imagePrediction!!)
                    binding.resultImage.setImageBitmap(bitmapImage)
                // Retrieve the prediction results and confidence scores
                val predictions: String? = prediction.predictionResults
                val confidence: Double? = prediction.confidenceScores
                // Update the TextView with the prediction results and confidence scores
                binding.resultText.text = "Results: $predictions\nConfidence: ${confidence.toString()}"
                setCardColorBasedOnPrediction(predictions)
                setExplanationBasedOnConfidence(confidence!!,predictions)

            })

        }

    }
    private fun setCardColorBasedOnPrediction(predictions: String?) {
        val colorRes = if (predictions?.equals("Cancer", ignoreCase = true) == true) {
            R.color.colorForCancer // Define this color in your colors.xml as #C32BA2
        } else {
            R.color.colorForNonCancer // Define this color in your colors.xml as #2bc3a7
        }
        binding.tvDescPenjelasan.text = "penjelasan"
        binding.materialCardWrapResultText.setCardBackgroundColor(ContextCompat.getColor(this, colorRes))
    }
    private fun setExplanationBasedOnConfidence(confidenceScore: Double, predictions: String?) {
         if(predictions?.equals("Cancer", ignoreCase = true) == true) {
            val explanation = when {
                confidenceScore in 0.0..10.0 -> """
            Hmm, tampaknya kemungkinan sangat rendah bahwa ini termasuk kanker. Tidak perlu khawatir terlalu banyak, tapi tetap perlu diingat untuk selalu memantau gejala yang baru muncul. Jangan ragu untuk menjaga kesehatan dan melakukan pemeriksaan rutin untuk kepastian.
            - Lakukan pemeriksaan kulit sendiri secara berkala untuk melihat perubahan apapun.
            - Gunakan tabir surya untuk melindungi kulit dari sinar matahari yang berlebihan.
            - Pertimbangkan untuk berkonsultasi tahunan dengan dermatologis sebagai tindakan pencegahan.
        """.trimIndent()

                confidenceScore in 10.0..20.0 -> """
            Sepertinya kemungkinan rendah bahwa ini termasuk kanker. Tetap jaga kesehatan dengan pola hidup yang sehat dan rajin melakukan pemeriksaan rutin. Ini bisa menjadi waktu yang tepat untuk mulai memperhatikan diet sehat dan rutin berolahraga.
            - Hindari paparan sinar matahari langsung terlalu lama, terutama di jam-jam puncak.
            - Pertimbangkan pemeriksaan rutin, seperti pemeriksaan tahunan, untuk memantau kesehatan kulit Anda.
        """.trimIndent()

                confidenceScore in 30.0..40.0 -> """
            Ada kemungkinan moderat bahwa ini termasuk kanker. Mungkin sedikit membuat khawatir, tapi jangan panik. Langkah terbaik adalah memeriksakan diri lebih lanjut untuk kepastian. Anda mungkin ingin segera berkonsultasi dengan dokter untuk mengetahui opsi-opsi pengobatan yang tersedia.
            - Jangan tunda untuk membuat janji dengan dokter Anda, terutama jika Anda memperhatikan perubahan baru atau mencemaskan pada kulit Anda.
            - Diskusikan dengan dokter tentang skrining dan pengujian yang dapat dilakukan untuk deteksi dini.
            - Jaga agar kulit Anda terhidrasi dan hindari faktor risiko lingkungan, seperti paparan bahan kimia berbahaya.
        """.trimIndent()
                confidenceScore in 50.0..75.0 -> """
            Wah, kemungkinan besar bahwa ini termasuk kanker. Jangan panik! Segera cari bantuan medis dan konsultasikan dengan dokter spesialis. 
            Mereka akan memberikan panduan yang baik mengenai langkah-langkah selanjutnya yang harus diambil. Tetap kuat dan positif, Anda tidak sendirian dalam perjalanan ini.
            
            Gejala umum kanker kulit meliputi:
            - Perubahan pada kulit yang tidak sembuh.
            - Bercak yang tumbuh dengan cepat.
            - Perubahan warna pada kulit, termasuk penebalan atau merah terang.
            - Lesi yang berdarah atau bersisik.
            
            Saran penanganan awal:
            - Jangan mengabaikan gejala. Catat perubahan apa saja pada kulit Anda.
            - Jadwalkan kunjungan dengan dermatologis atau dokter kulit untuk pemeriksaan mendalam.
            - Lindungi kulit Anda dari paparan sinar matahari berlebih dengan menggunakan tabir surya.
        """.trimIndent()

                confidenceScore in 75.0..100.0 -> """
            Sangat mungkin ini adalah kanker. 
            Ambil tindakan medis segera dan mulailah diskusi yang mendalam dengan dokter Anda mengenai pilihan pengobatan. Ingatlah bahwa Anda memiliki dukungan penuh dari keluarga, teman, dan tenaga medis untuk membantu Anda melalui ini.
            
            Gejala yang harus diwaspadai mencakup:
            - Lesi yang tidak sembuh, bahkan setelah beberapa minggu.
            - Benjolan atau bercak yang berwarna merah muda, putih, atau biru.
            - Perubahan pada tahi lalat yang ada, termasuk ukuran, bentuk, atau warna.
            
            Langkah-langkah yang direkomendasikan:
            - Segera temui dokter spesialis untuk evaluasi lebih lanjut.
            - Pertimbangkan biopsi untuk mengonfirmasi diagnosis.
            - Diskusikan pilihan pengobatan yang mungkin, termasuk operasi, radiasi, atau kemoterapi.
            - Pertimbangkan untuk mendapatkan pendapat kedua jika Anda merasa perlu.
        """.trimIndent()
                else -> "Tingkat kepercayaan ini sangat tidak umum dan sulit dipahami. Ini mungkin perlu peninjauan lebih lanjut dan diskusi mendalam dengan dokter Anda. Jangan ragu untuk mencari bantuan dan meminta penjelasan lebih lanjut. Tetaplah berpikiran terbuka dan positif, Anda memiliki kemampuan untuk mengatasi ini."
            }
            binding.tvDescPenjelasan.text = explanation
        } else {
             val explanations = when {
                 confidenceScore in 0.0..25.0 -> """
        Kemungkinan tinggi ini adalah kanker. Penting untuk segera berkonsultasi dengan dokter:
        - Amati perubahan pada kulit atau tahi lalat secara teratur.
        - Segera hubungi dokter jika Anda melihat perubahan yang mencurigakan.
        - Gunakan tabir surya secara teratur dan hindari paparan sinar UV berlebihan.
    """.trimIndent()

                 confidenceScore in 25.0..50.0 -> """
        Kemungkinan cukup tinggi ini adalah kanker. Saran untuk segera memeriksakan diri:
        - Lakukan evaluasi lebih lanjut dengan dokter spesialis kulit.
        - Tinjau ulang gaya hidup Anda, termasuk pola makan dan paparan terhadap sinar matahari.
        - Tetap awasi perubahan pada kulit dan segera konsultasikan dengan dokter jika diperlukan.
    """.trimIndent()

                 confidenceScore in 50.0..100.0 -> """
        Kemungkinan rendah ini adalah kanker, namun tetaplah waspada dan lakukan pemeriksaan rutin:
        - Perhatikan perubahan pada kulit dan konsultasikan dengan dokter jika ada yang mencurigakan.
        - Terus gunakan tabir surya dan hindari paparan sinar UV berlebihan.
        - Pertahankan gaya hidup sehat dengan pola makan seimbang dan cukup olahraga.
    """.trimIndent()

                 else -> {}
             }

             binding.tvDescPenjelasan.text = explanations.toString();

         }

    }

}