package com.example.team24app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

// home의 리사이클러뷰 어댑터
class PostAdapter(val itemList: MutableList<Post>, val context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    //  뷰 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    // 내용 입력(리사이클 될때마다 입력됨)
    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        // 뷰에 내용 입력(리사이클 될때마다 입력됨)

        if(itemList[position].profile!="tmp"){
            val uri_profile = Uri.fromFile(File(itemList[position].profile))
            holder.ivProfile.setImageURI(uri_profile)
        }else{
            holder.ivProfile.setImageResource(R.drawable.default_profile)
        }

        val uri_picture = Uri.fromFile(File(itemList[position].picture))
        var like = itemList[position].like
        var isClieked = itemList[position].isClicked
        if (isClieked){ holder.btnLike.setBackgroundResource(R.drawable.like_filled) }

        holder.tvName.text = itemList[position].id
        holder.ivPicture.setImageURI(uri_picture)
        holder.tvLike.text = "$like"
        holder.tvDescrip.text = itemList[position].comment
        holder.tvDate.text = itemList[position].date
        holder.tvHour.text = "${itemList[position].hour}"
        holder.tvMin.text = "${itemList[position].minute}"
        holder.tvSec.text = "${itemList[position].second}"

        holder.btnLike.setOnClickListener {
            val dbManager = DBManager(context, "appDB", null, 1)
            val sqlitedb = dbManager.writableDatabase

            val user_id = UserId.userId
            val post_id = itemList[position].post_id

            isClieked = !isClieked
            if(isClieked){
                like++
                sqlitedb.execSQL("UPDATE post SET num_like = ? WHERE post_id = ?", arrayOf(like, post_id))
                sqlitedb.execSQL("UPDATE clickLike SET isClicked = 1 WHERE user_id = ? AND post_id = ?", arrayOf(user_id, post_id))
                holder.tvLike.text = "$like"
                holder.btnLike.setBackgroundResource(R.drawable.like_filled)
            }else{
                like--
                sqlitedb.execSQL("UPDATE post SET num_like = ? WHERE post_id = ?", arrayOf(like, post_id))
                sqlitedb.execSQL("UPDATE clickLike SET isClicked = 0 WHERE user_id = ? AND ?", arrayOf(user_id, post_id))
                holder.tvLike.text = "$like"
                holder.btnLike.setBackgroundResource(R.drawable.like_btn)
            }

            sqlitedb.close()
            dbManager.close()
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    // 리사이클러뷰의 위젯 선언 및 연결
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
        var btnLike = itemView.findViewById<ImageButton>(R.id.btnLike)

        init {
            // 포스트를 클릭하면 해당 clickPost로 이동
            itemView.setOnClickListener {
                val intent = Intent(context, ClickPost::class.java)
                intent.putExtra("post_id", itemList[adapterPosition].post_id)
                context.startActivity(intent)
            }
        }
    }
}

