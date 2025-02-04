package com.example.team24app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

//  그리드 리사이클러뷰 어댑터
class SquareAdapter(val itemList: MutableList<Post_Square>, val context : Context) : RecyclerView.Adapter<SquareAdapter.ViewHolder>() {
    // 뷰 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SquareAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_square_item, parent, false)
        return ViewHolder(view)
    }

    // 내용 입력(리사이클 될때마다 입력됨)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = Uri.fromFile(File(itemList[position].picture))
        holder.ivPicture.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPicture = itemView.findViewById<ImageView>(R.id.ivFeed)

        init {
            //뷰 클릭 리스너(포스트 터치시 ClickPost로 이동)
            itemView.setOnClickListener {
                val intent = Intent(context, ClickPost::class.java)
                intent.putExtra("post_id", itemList[adapterPosition].post_id)
                context.startActivity(intent)
            }
        }
    }
}