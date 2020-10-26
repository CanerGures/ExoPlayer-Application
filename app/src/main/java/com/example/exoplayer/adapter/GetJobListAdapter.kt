package com.example.exoplayer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.MainActivity
import com.example.exoplayer.R
import com.example.exoplayer.api.model.GetJobsList

class GetJobListAdapter(
        private val features: List<GetJobsList>
) : RecyclerView.Adapter<GetJobListAdapter.GetJobListViewHolder>() {
    inner class GetJobListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageUrlMain: ImageView = itemView.findViewById(R.id.imagePoster)
        val textTitle: TextView = itemView.findViewById(R.id.textTitlePoster)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetJobListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_dashboard_cards, parent, false)
        return GetJobListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return features.size
    }

    override fun onBindViewHolder(holder: GetJobListViewHolder, position: Int) {
        val currentItem = features[position]
        holder.textTitle.text = currentItem.name
        /*Glide.with(holder.itemView.context)
                .load(currentItem.)
                .fitCenter()
                .into(holder.imageUrlMain)*/

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MainActivity::class.java)
            intent.putExtra("currentItem", currentItem)
            it.context.startActivity(intent)

        }
    }
}