package com.example.exoplayer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exoplayer.MainActivity
import com.example.exoplayer.R
import com.example.exoplayer.model.MainDashboardModel

class MainItemsRecyclerAdapter(
    private val features: List<MainDashboardModel>
) : RecyclerView.Adapter<MainItemsRecyclerAdapter.DashboardViewHolder>() {
    inner class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageUrlMain: ImageView = itemView.findViewById(R.id.imagePoster)
        val textTitle: TextView = itemView.findViewById(R.id.textTitlePoster)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_dashboard_cards, parent, false)
        return DashboardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return features.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val currentItem = features[position]
        holder.textTitle.text = currentItem.title
        Glide.with(holder.itemView.context)
            .load(currentItem.posterUrl)
            .fitCenter()
            .into(holder.imageUrlMain)

        holder.itemView.setOnClickListener {


            when (currentItem.id) {
                1 -> {
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentItem", currentItem)
                    it.context.startActivity(intent)

                }
                2 -> {
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentItem", currentItem)
                    it.context.startActivity(intent)

                }
                3 -> {
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentItem", currentItem)
                    it.context.startActivity(intent)


                }
                4 -> {
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentItem", currentItem)
                    it.context.startActivity(intent)

                }


            }


        }
    }
}