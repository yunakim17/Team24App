package com.example.team24app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//Simple_Profile라는 이름의 data class타입의 arrayList 사용
class ProfileAdpater(val itemList: ArrayList<Simple_Profile>) : RecyclerView.Adapter<ProfileAdpater.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileAdpater.ViewHolder {
        //뷰 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdpater.ViewHolder, position: Int) {
        // 뷰에 내용 입력(리사이클 될때마다 입력됨)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //해당 위치에 profile의 위젯들을 변수 선언함
    }
}