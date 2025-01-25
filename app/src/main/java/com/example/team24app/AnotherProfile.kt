package com.example.team24app

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnotherProfile : AppCompatActivity() {
    lateinit var name : TextView
    lateinit var profile : ImageView
    lateinit var friend : TextView
    lateinit var description : TextView
    lateinit var btnAdd : Button
    lateinit var rvPost : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_profile)

        name = findViewById<TextView>(R.id.tvUserName)
        profile = findViewById<ImageView>(R.id.ivProfileImage)
        friend = findViewById<TextView>(R.id.tvFriendCount)
        description = findViewById<TextView>(R.id.tvDescription)
        btnAdd = findViewById<Button>(R.id.btnAddFriend)
        rvPost = findViewById<RecyclerView>(R.id.rvPosts)
        val itemlist = ArrayList<Post>()

        //테이블에서 상대 프로필 정보를 끌고옴
        //친구 버튼을 제외한 목록을 채움

        val rv_adapter = PostAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        rvPost.adapter=rv_adapter
        rvPost.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        btnAdd.setOnClickListener {
            //유저 테이블에서 친구 데이터를 생성해야함
        }
    }
}