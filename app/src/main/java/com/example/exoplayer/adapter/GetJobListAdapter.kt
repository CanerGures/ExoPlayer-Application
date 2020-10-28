package com.example.exoplayer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exoplayer.MainActivity
import com.example.exoplayer.R
import com.example.exoplayer.api.model.GetJobsList
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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
        val link = "https://" + currentItem.streamLink
        val lastThree = link.takeLast(3)
        if (lastThree == "mpd") {
            var jpgLink = link.dropLast(3)
            jpgLink += "jpg"

            Glide.with(holder.itemView.context)
                .load(jpgLink)
                .fitCenter()
                .into(holder.imageUrlMain)
        }


        holder.itemView.setOnClickListener {
            if (lastThree == "xml") {
                val xmlLink = currentItem.streamLink
                val link = xmlLink.takeLast(17)
                val model = getXml(link)
                val s = model.string()
                if (s.length < 250) {
                    val m = s.drop(75)
                    val k = m.dropLast(118)
                    val linkFromXml = "https://${k}"
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentLink", linkFromXml)
                    it.context.startActivity(intent)
                } else {
                    val m = s.drop(80)
                    val k = m.dropLast(208)
                    val linkFromXml = "https://${k}"
                    val intent = Intent(it.context, MainActivity::class.java)
                    intent.putExtra("currentLink", linkFromXml)
                    it.context.startActivity(intent)
                }
                Toast.makeText(holder.itemView.context, model.string(), Toast.LENGTH_LONG).show()

            } else {
                val intent = Intent(it.context, MainActivity::class.java)
                intent.putExtra("currentItem", currentItem)
                it.context.startActivity(intent)
            }

        }
    }


    private fun getXml(link: String): ResponseBody {
        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://cdn.videobulut.com/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
        val xmlService: XmlGet = retrofit.create(XmlGet::class.java)
        val call: Call<ResponseBody> = xmlService.getXml(link)
        val response: Response<ResponseBody> = call.execute()
        return response.body()
    }

    interface XmlGet{
        @GET("{url}")
        fun getXml(@Path("url") link: String): Call<ResponseBody>
    }
}