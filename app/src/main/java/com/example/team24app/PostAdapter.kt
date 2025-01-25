package com.example.team24app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Feed라는 이름의 data class타입의 arrayList 사용
class PostAdapter(val itemList: ArrayList<Post>, val context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostAdapter.ViewHolder {
        //뷰 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        // 뷰에 내용 입력(리사이클 될때마다 입력됨)
        holder.ivProfile.setImageResource(itemList[position].profile)
        holder.tvName.text = itemList[position].id
        holder.ivPost.setImageResource(itemList[position].img)
        holder.tvLike.text = itemList[position].like
        holder.tvDescrip.text = itemList[position].comment
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivProfile = itemView.findViewById<ImageView>(R.id.ivProfilePic)
        var tvName = itemView.findViewById<TextView>(R.id.tvUserName)
        var ivPost = itemView.findViewById<ImageView>(R.id.ivPostImg)
        var tvLike = itemView.findViewById<TextView>(R.id.tvLikes)
        var tvDescrip = itemView.findViewById<TextView>(R.id.tvPostDescription)

        init {
            //뷰 클릭 리스너
            itemView.setOnClickListener {
                val intent = Intent(context, ClickFeed::class.java)
                intent.putExtra("post", itemList[adapterPosition])
                context.startActivity(intent)
            }
        }
    }
}

