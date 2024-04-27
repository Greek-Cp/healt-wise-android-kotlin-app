package com.dicoding.asclepius.view.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.PredictionModel
import com.dicoding.asclepius.view.util.ImageUtils
import com.google.android.material.card.MaterialCardView


class AdapterRiwayatScan(private var predictions: List<PredictionModel>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<AdapterRiwayatScan.ViewHolder>() {
    private fun setCardColorBasedOnPrediction(predictions: String?, itemView: View) {
        val colorRes = if (predictions?.equals("Cancer", ignoreCase = true) == true) {
            R.color.colorForCancer
        } else {
            R.color.colorForNonCancer
        }
        itemView.findViewById<MaterialCardView>(R.id.id_card_status).setCardBackgroundColor(ContextCompat.getColor(itemView.context, colorRes))
    }
    private fun setExplanationBasedOnConfidence(confidenceScore: Double, predictions: String?): String{
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
          return explanation
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

            return explanations.toString();

        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PredictionModel, isSelected: Boolean){
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout)
                .setOnClickListener{
                    onItemClick(item.id.toString())
                }
            val tv_status_scan = itemView.findViewById<TextView>(R.id.tvStatusDeteksi)
            tv_status_scan.setText(item.predictionResults)
            setCardColorBasedOnPrediction(item.predictionResults,itemView)
            val tv_deskripsi_scan = itemView.findViewById<TextView>(R.id.id_tv_status_penjelasan)
            tv_deskripsi_scan.text = setExplanationBasedOnConfidence(item.confidenceScores,item.predictionResults)
            val imageView = itemView.findViewById<ImageView>(R.id.id_img_scan)
            imageView.setImageBitmap(ImageUtils.byteArrayToBitmap(item.imagePrediction!!))
            val tv_waktu = itemView.findViewById<TextView>(R.id.id_tv_waktu);
            tv_waktu.setText(item.timestamp)
            itemView.setOnClickListener {
                if (selectedPosition != adapterPosition) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }
    var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = predictions[position]
        val isSelected = position == selectedPosition
        holder.bind(category, isSelected)
    }
    override fun getItemCount(): Int = predictions.size
    fun updatepredictions(newpredictions: List<PredictionModel>) {
        this.predictions = newpredictions
        notifyDataSetChanged()
    }
}