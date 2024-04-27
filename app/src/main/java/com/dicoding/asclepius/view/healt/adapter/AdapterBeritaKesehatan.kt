package com.dicoding.asclepius.view.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.ArticlesItem
import com.google.android.material.card.MaterialCardView


class AdapterBeritaKesehatan(private var articleItem: List<ArticlesItem>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<AdapterBeritaKesehatan.ViewHolder>() {
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

            Glide.with(itemView.context)
                .load(R.drawable.asset_wise)
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