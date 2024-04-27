package com.dicoding.asclepius.view.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.ArticlesItem
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.card.MaterialCardView


class AdapterBeritaKesehatan(private var articleItem: List<ArticlesItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<AdapterBeritaKesehatan.ViewHolder>() {
    private fun setCardColorBasedOnPrediction(articleItem: String?, itemView: View) {
        val colorRes = if (articleItem?.equals("Cancer", ignoreCase = true) == true) {
            R.color.colorForCancer // Define this color in your colors.xml as #C32BA2
        } else {
            R.color.colorForNonCancer // Define this color in your colors.xml as #2bc3a7
        }
        itemView.findViewById<MaterialCardView>(R.id.id_card_status).setCardBackgroundColor(ContextCompat.getColor(itemView.context, colorRes))
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ArticlesItem, isSelected: Boolean){
            itemView.findViewById<MaterialCardView>(R.id.id_card_layout)
                .setOnClickListener{
                    onItemClick(item.url.toString())
                }
            val tv_status_scan = itemView.findViewById<TextView>(R.id.tvStatusDeteksi)
            tv_status_scan.setText(item.author)
            val tv_deskripsi_scan = itemView.findViewById<TextView>(R.id.id_tv_status_penjelasan)
            val imageView = itemView.findViewById<ImageView>(R.id.id_img_scan)
            val tv_waktu = itemView.findViewById<TextView>(R.id.id_tv_waktu)
            tv_deskripsi_scan.text = item.title.toString()
            tv_waktu.setText(item.publishedAt)
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(1800)
                .setBaseAlpha(0.7f)
                .setHighlightAlpha(0.6f)
                .setDirection(Shimmer.Direction.RIGHT_TO_LEFT)
                .setAutoStart(true)
                .build()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }
            Glide.with(itemView.context)
                .load(R.drawable.asset_wise)
                .placeholder(shimmerDrawable)
                .into(imageView)
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
    fun updateData(newData: List<ArticlesItem>) {
        this.articleItem = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = articleItem[position]
        val isSelected = position == selectedPosition
        holder.bind(category, isSelected)
    }
    override fun getItemCount(): Int = articleItem.size
    fun updatearticleItem(newarticleItem: List<ArticlesItem>) {
        this.articleItem = newarticleItem
        notifyDataSetChanged()
    }
}