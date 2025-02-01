package com.example.team24app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        edtSearch = findViewById(R.id.edtSearchBar)
        btnSearch = findViewById(R.id.ivSearch)
        rvProfile = findViewById(R.id.rvSearchUser)

        val intent = intent
        edtSearch.setText(intent.getStringExtra("search_id"))
        //홈 화면에서 검색한 아이디를 edt에 삽입

        reSearch()
        //홈 화면에서 검색한 결과 리사이클러뷰에 로드

        btnSearch.setOnClickListener{
            reSearch()
            //재검색시 다시 리사이클러뷰에 로드
        }
    }

    private fun reSearch(){
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        val search_id = edtSearch.text.toString()
        val itemlist = ArrayList<User>()

        if(search_id.isNotBlank()){
            var cursor : Cursor
            val user_id = UserId.userId
            cursor = sqlitedb.rawQuery("SELECT user_id, profile FROM user WHERE user_id like '%${search_id}%' AND user_id != '${user_id}';", null)
            //검색한 문자열을 포함하고 있는 아이디를 추출

            while(cursor.moveToNext()){
                val user_id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"))
                val profile = cursor.getString(cursor.getColumnIndexOrThrow("profile"))
                val item = User(profile, user_id)
                itemlist.add(item)
            }
            cursor.close()
        }
        sqlitedb.close()
        dbManager.close()
        //테이블에서 아이디를 검색해 itemlist에 내용을 넣음

        val rv_adapter = ProfileAdpater(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        rvProfile.adapter = rv_adapter
        rvProfile.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //리사이클러뷰 적용
    }
}