package com.example.team24app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Search : AppCompatActivity() {
    lateinit var edtSearch : EditText
    lateinit var btnSearch : ImageButton
    lateinit var rvProfile : RecyclerView
    //유저 테이블에서 자료 들고와야함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        edtSearch = findViewById<EditText>(R.id.edtSearchBar)
        btnSearch = findViewById<ImageButton>(R.id.ivSearch)
        rvProfile = findViewById<RecyclerView>(R.id.rvSearchUser)

        val intent = intent
        edtSearch.setText(intent.getStringExtra("Search_id").toString())
        //홈 화면에서 검색한 아이디를 edt에 삽입

        reSearch()
        //홈 화면에서 검색한 결과 리사이클러뷰에 로드

        btnSearch.setOnClickListener{
            reSearch()
            //재검색시 다시 리사이클러뷰에 로드
        }
    }

    private fun reSearch(){
        val search_id = edtSearch.text.toString()
        val itemlist = ArrayList<User>()

        //테이블에서 아이디를 검색해 itemlist에 내용을 넣어야함

        val rv_adapter = ProfileAdpater(itemlist)
        rv_adapter.notifyDataSetChanged()
        rvProfile.adapter = rv_adapter
        rvProfile.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}