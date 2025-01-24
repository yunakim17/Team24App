package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Profile : AppCompatActivity() {
    lateinit var name : TextView
    lateinit var profile : ImageView
    lateinit var friend : TextView
    lateinit var description : TextView
    lateinit var btnProfile : Button
    lateinit var rvPost : RecyclerView
    //db 불러오기 필수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById<TextView>(R.id.tvUserName)
        profile = findViewById<ImageView>(R.id.ivProfileImage)
        friend = findViewById<TextView>(R.id.tvFriendCount)
        description = findViewById<TextView>(R.id.tvDescription)
        btnProfile = findViewById<Button>(R.id.btnEdtProfile)
        rvPost = findViewById<RecyclerView>(R.id.rvPosts)
        val itemlist = ArrayList<Feed>()


        //테이블에서 유저 정보 가져와서 text 변경

        //자신의 피드 목록의 디자인이 필요하다면 따로 제작 필요!!(임시로 feed_list.xml의 디자인을 가져옴)

        val rv_adapter = RecyclerViewAdapter(itemlist)
        rv_adapter.notifyDataSetChanged()
        rvPost.adapter=rv_adapter
        rvPost.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //리사이클러뷰 어댑터 연결 완료

        btnProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
            //프로필 편집 화면으로 전환
        }

    }
}