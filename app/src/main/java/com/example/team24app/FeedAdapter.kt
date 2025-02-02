package com.example.team24app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(val itemList: ArrayList<Feed_Square>, val context : Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedAdapter.ViewHolder {
        //뷰 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_square_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //picture 적용
        //val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        val uri = Uri.parse(itemList[position].picture)
        //context.contentResolver.takePersistableUriPermission(uri, flag)
        holder.ivPicture.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPicture = itemView.findViewById<ImageView>(R.id.ivFeed)

        init {
            //뷰 클릭 리스너(프로필 터치 시, 상대 프로필이 뜸)
            itemView.setOnClickListener {
                val intent = Intent(context, ClickFeed::class.java)
                intent.putExtra("post_id", itemList[adapterPosition].post_id)
                context.startActivity(intent)
            }
        }
    }
}