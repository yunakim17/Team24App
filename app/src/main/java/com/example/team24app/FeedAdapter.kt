package com.example.team24app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Feed라는 이름의 data class타입의 arrayList 사용
class FeedAdapter(val itemList: ArrayList<Post>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedAdapter.ViewHolder {
        //뷰 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        // 뷰에 내용 입력(리사이클 될때마다 입력됨)
        holder.ivProfile = itemList[position].userProfile
        holder.tvName = itemList[position].userId
        holder.ivPost = itemList[position].imgPost
        holder.tvLike = itemList[position].numLike
        holder.tvDescrip = itemList[position].comment
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
    }
}

