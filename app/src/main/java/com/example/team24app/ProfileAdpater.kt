package com.example.team24app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Simple_Profile라는 이름의 data class타입의 arrayList 사용
class ProfileAdpater(val itemList: ArrayList<User>) : RecyclerView.Adapter<ProfileAdpater.ViewHolder>(){
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
        holder.ivProfile = itemList[position].userProfile
        holder.tvName = itemList[position].userId
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivProfile = itemView.findViewById<ImageView>(R.id.ivProfilePic)
        var tvName = itemView.findViewById<TextView>(R.id.tvUserName)
    }
}