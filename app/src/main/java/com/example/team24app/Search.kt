package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

// 검색 화면
class Search : AppCompatActivity() {
    lateinit var edtSearch : EditText
    lateinit var btnSearch : ImageButton
    lateinit var rvProfile : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        edtSearch = findViewById(R.id.edtSearchBar)
        btnSearch = findViewById(R.id.ivSearch)
        rvProfile = findViewById(R.id.rvSearchUser)

        // 하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()


        // 홈 화면에서 검색한 아이디 데이터 입력 / 검색
        val intent = intent
        edtSearch.setText(intent.getStringExtra("search_id"))
        reSearch()

        // 검색 버튼
        btnSearch.setOnClickListener{
            reSearch()
        }
    }

    // 아이디 검색, 리사이클러뷰 로드
    private fun reSearch(){
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase
        val search_id = edtSearch.text.toString()
        val itemList = ArrayList<User>()

        if(search_id.isNotBlank()){
            var cursor : Cursor
            val user_id = UserId.userId
            cursor = sqlitedb.rawQuery("SELECT user_id, profile FROM user WHERE user_id like ? AND user_id != ?", arrayOf(search_id, user_id))
            //검색한 문자열을 포함하고 있는 아이디를 추출

            while(cursor.moveToNext()){
                val user_id = cursor.getString(cursor.getColumnIndexOrThrow("user_id"))
                val profile = cursor.getString(cursor.getColumnIndexOrThrow("profile"))
                val item = User(profile, user_id)
                itemList.add(item)
            }
            cursor.close()
        }
        sqlitedb.close()
        dbManager.close()
        //테이블에서 아이디를 검색해 itemlist에 내용을 넣음

        // 리사이클러뷰 적용
        val rv_adapter = ProfileAdpater(itemList, this)
        rv_adapter.notifyDataSetChanged()
        rvProfile.adapter = rv_adapter
        rvProfile.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    // 하단 네비게이션바 기능 추가
    fun setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }

                R.id.timer -> {
                    val intent = Intent(this, com.example.team24app.Timer::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}