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

// 아이디 검색 리사이클러뷰 어댑터
class ProfileAdpater(val itemList: MutableList<User>, val context : Context) : RecyclerView.Adapter<ProfileAdpater.ViewHolder>(){
    // 뷰 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileAdpater.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    // 내용 입력(리사이클 될때마다 입력됨)
    override fun onBindViewHolder(holder: ProfileAdpater.ViewHolder, position: Int) {
        // 사용자의 프로필이 비어있다면 기본 이미지로 설정
        if(itemList[position].profile != "tmp"){
            val uri = Uri.fromFile(File(itemList[position].profile))
            holder.ivProfile.setImageURI(uri)
        }else{
            holder.ivProfile.setImageResource(R.drawable.default_profile)
        }

        holder.tvName.text = itemList[position].id
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivProfile = itemView.findViewById<ImageView>(R.id.ivProfilePic)
        var tvName = itemView.findViewById<TextView>(R.id.tvUserName)

        init {
            // 뷰 클릭 리스너(프로필 터치 시, 상대 프로필이 뜸)
            itemView.setOnClickListener {
                val intent = Intent(context, AnotherProfile::class.java)
                intent.putExtra("user_id", itemList[adapterPosition].id)
                context.startActivity(intent)
            }
        }
    }
}