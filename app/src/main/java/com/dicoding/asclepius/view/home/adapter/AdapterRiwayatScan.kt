package com.dicoding.asclepius.view.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.PredictionModel
import com.dicoding.asclepius.view.util.ImageUtils
import com.google.android.material.card.MaterialCardView


class AdapterRiwayatScan(private var predictions: List<PredictionModel>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<AdapterRiwayatScan.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PredictionModel, isSelected: Boolean){
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout)
                .setOnClickListener{
                    onItemClick(item.id.toString())
                }
            val tv_status_scan = itemView.findViewById<TextView>(R.id.tvStatusDeteksi)
            tv_status_scan.setText(item.predictionResults)
            val tv_deskripsi_scan = itemView.findViewById<TextView>(R.id.id_tv_status_penjelasan)
            tv_deskripsi_scan.setText("get data")

            val imageView = itemView.findViewById<ImageView>(R.id.id_img_scan)
            imageView.setImageBitmap(ImageUtils.byteArrayToBitmap(item.imagePrediction!!))

//            val shimmer = Shimmer.AlphaHighlightBuilder()
//                .setDuration(1800)
//                .setBaseAlpha(0.7f)
//                .setHighlightAlpha(0.6f)
//                .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
//                .setAutoStart(true)
//                .build()
//            val shimmerDrawable = ShimmerDrawable().apply {
//                setShimmer(shimmer)
//            }
//            Glide.with(itemView.context)
//                .load(item.avatarUrl)
//                .placeholder(shimmerDrawable)
//                .into(imageView)

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
        Log.d("update", "Update predictions")
        this.predictions = newpredictions
        notifyDataSetChanged()
    }
}