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

class ProfileAdpater(val itemList: ArrayList<User>, val context : Context) : RecyclerView.Adapter<ProfileAdpater.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileAdpater.ViewHolder {
        //뷰 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdpater.ViewHolder, position: Int) {
        // 테이블에서 가져와 kt로 전달된 데이터를 이곳에서 입력
        if(itemList[position].profile != "tmp"){
            val uri = Uri.parse(itemList[position].profile)
            holder.ivProfile.setImageURI(uri)
        }else{
            holder.ivProfile.setImageResource(R.drawable.img)
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
            //뷰 클릭 리스너(프로필 터치 시, 상대 프로필이 뜸)
            itemView.setOnClickListener {
                val intent = Intent(context, AnotherProfile::class.java)
                intent.putExtra("user_id", itemList[adapterPosition].id)
                context.startActivity(intent)
            }
        }
    }
}