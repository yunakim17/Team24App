package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Home : AppCompatActivity() {
    lateinit var edtSearch : EditText
    lateinit var btnSearch : ImageButton
    lateinit var rvHome : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        edtSearch = findViewById<EditText>(R.id.edtSearchBar)
        var search_id = edtSearch.text.toString()
        btnSearch = findViewById<ImageButton>(R.id.ivSearch)
        rvHome = findViewById<RecyclerView>(R.id.rvHomeFeed)
        val itemList = ArrayList<Feed>()

        //이곳에 itemList에 친구의 피드 요소들을 더해 item을 늘림
        //feed의 구성이 완료되면 추가 예정

        val rv_adapter = RecyclerViewAdapter(itemList)
        rv_adapter.notifyDataSetChanged()
        //어댑터와 리사이클러뷰 갱신

        rvHome.adapter=rv_adapter
        rvHome.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //어댑터를 연결해 레이아웃 매니저를 설정(리사이클러뷰 설정 완료)

        btnSearch.setOnClickListener{
            val intent = Intent(this, Search::class.java)
            intent.putExtra("Search_id", search_id)
            startActivity(intent)
        }
    }
}