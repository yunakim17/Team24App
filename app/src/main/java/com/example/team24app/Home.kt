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

class Home : AppCompatActivity() {
    lateinit var edtSearch : EditText
    lateinit var btnSearch : ImageButton
    lateinit var rvHome : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        edtSearch = findViewById(R.id.edtSearchBar)
        btnSearch = findViewById(R.id.ivSearch)
        rvHome = findViewById(R.id.rvHomeFeed)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        val user_id = UserId.userId
        val itemlist = ArrayList<Post>()

        var cursor_friend : Cursor
        cursor_friend = sqlitedb.rawQuery("SELECT to_id FROM friend WHERE from_id = '${user_id}';", null)
        //유저의 친구 목록 가져옴

        while(cursor_friend.moveToNext()){
            //친구 목록 탐색
            val friend_id = cursor_friend.getString((cursor_friend.getColumnIndexOrThrow("to_id"))).toString()

            var profile = ""
            var cursor_user : Cursor
            cursor_user = sqlitedb.rawQuery("SELECT profile FROM user WHERE user_id = '${friend_id}';", null)
            //프로필 먼저 추출

            if (cursor_user.moveToNext()){
                profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
            }
            cursor_user.close()

            var cursor_post : Cursor
            cursor_post = sqlitedb.rawQuery("SELECT * FROM post WHERE user_id = '${friend_id}';", null)

            while (cursor_post.moveToNext()){
                val post_id = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("post_id"))
                val picture = cursor_post.getString(cursor_post.getColumnIndexOrThrow("picture"))
                val like = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("num_like"))
                val comment = cursor_post.getString(cursor_post.getColumnIndexOrThrow("comment"))
                val date = cursor_post.getString(cursor_post.getColumnIndexOrThrow("date"))
                val hour = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("hour"))
                val minute = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("minute"))
                val second = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("second"))
                val item = Post(profile, friend_id, post_id, picture, like, comment, date, hour, minute, second)
                itemlist.add(item)
                //itemList에 친구의 피드 요소들을 더해 item을 늘림
            }
            cursor_post.close()
        }
        cursor_friend.close()

        val rv_adapter = PostAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        //어댑터와 리사이클러뷰 갱신

        rvHome.adapter=rv_adapter
        rvHome.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //어댑터를 연결해 레이아웃 매니저를 설정(리사이클러뷰 설정 완료)

        sqlitedb.close()
        dbManager.close()

        btnSearch.setOnClickListener{
            val search_id = edtSearch.text.toString()
            edtSearch.setText("")
            val intent = Intent(this, Search::class.java)
            intent.putExtra("search_id", search_id)
            startActivity(intent)
            //검색 화면으로 전환
        }
    }
}