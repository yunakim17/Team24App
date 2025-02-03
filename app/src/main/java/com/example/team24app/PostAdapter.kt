package com.example.team24app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

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

        if(itemList[position].profile!="tmp"){
            val uri_profile = Uri.fromFile(File(itemList[position].profile))
            holder.ivProfile.setImageURI(uri_profile)
        }else{
            holder.ivProfile.setImageResource(R.drawable.img)
        }

        val uri_picture = Uri.fromFile(File(itemList[position].picture))

        holder.tvName.text = itemList[position].id
        holder.ivPicture.setImageURI(uri_picture)
        holder.tvLike.text = "${itemList[position].like}"
        holder.tvDescrip.text = itemList[position].comment
        holder.tvDate.text = itemList[position].date
        holder.tvHour.text = "${itemList[position].hour}"
        holder.tvMin.text = "${itemList[position].minute}"
        holder.tvSec.text = "${itemList[position].second}"
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivProfile = itemView.findViewById<ImageView>(R.id.ivProfilePic)
        var tvName = itemView.findViewById<TextView>(R.id.tvUserName)
        var ivPicture = itemView.findViewById<ImageView>(R.id.ivPostImg)
        var tvLike = itemView.findViewById<TextView>(R.id.tvLikes)
        var tvDescrip = itemView.findViewById<TextView>(R.id.tvPostDescription)
        var tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        var tvHour = itemView.findViewById<TextView>(R.id.tvHours)
        var tvMin = itemView.findViewById<TextView>(R.id.tvMinutes)
        var tvSec = itemView.findViewById<TextView>(R.id.tvSeconds)

        init {
            //뷰 클릭 리스너
            itemView.setOnClickListener {
                val intent = Intent(context, ClickFeed::class.java)
                intent.putExtra("post_id", itemList[adapterPosition].post_id)
                context.startActivity(intent)
            }
        }
    }
}

