package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    lateinit var edtSearch : EditText
    lateinit var btnSearch : ImageButton
    lateinit var rvHome : RecyclerView

    //피드에 아이템이 없을 때 보이는 텍스트뷰/이미지뷰
    lateinit var emptyViewH: TextView
    lateinit var emptyImageH: ImageView

    //하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        edtSearch = findViewById(R.id.edtSearchBar)
        btnSearch = findViewById(R.id.ivSearch)
        rvHome = findViewById(R.id.rvHomeFeed)

        //텍스트,이미지뷰 연결
        emptyViewH = findViewById(R.id.noItemTextHome)
        emptyImageH = findViewById(R.id.noItemImageHome)

        //하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        btnSearch.setOnClickListener{
            val search_id = edtSearch.text.toString()
            edtSearch.setText("")
            val intent = Intent(this, Search::class.java)
            intent.putExtra("search_id", search_id)
            startActivity(intent)
            //검색 화면으로 전환
        }
    }

    override fun onStart() {
        super.onStart()
        val dbManager = DBManager(this, "appDB", null, 1)
        val sqlitedb = dbManager.readableDatabase

        val user_id = UserId.userId
        val itemlist = ArrayList<Post>()

        var cursor_follow : Cursor
        cursor_follow = sqlitedb.rawQuery("SELECT to_id FROM follow WHERE from_id = '${user_id}';", null)
        //유저의 친구 목록 가져옴

        while(cursor_follow.moveToNext()){
            //친구 목록 탐색
            val follow_id = cursor_follow.getString((cursor_follow.getColumnIndexOrThrow("to_id"))).toString()

            var profile = ""
            var cursor_user : Cursor
            cursor_user = sqlitedb.rawQuery("SELECT profile FROM user WHERE user_id = '${follow_id}';", null)
            //프로필 먼저 추출

            if (cursor_user.moveToNext()){
                profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
            }
            cursor_user.close()

            var cursor_post : Cursor
            cursor_post = sqlitedb.rawQuery("SELECT * FROM post WHERE user_id = '${follow_id}';", null)

            while (cursor_post.moveToNext()){
                val post_id = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("post_id"))
                val picture = cursor_post.getString(cursor_post.getColumnIndexOrThrow("picture"))
                val like = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("num_like"))
                val comment = cursor_post.getString(cursor_post.getColumnIndexOrThrow("comment"))
                val date = cursor_post.getString(cursor_post.getColumnIndexOrThrow("date"))
                val hour = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("hour"))
                val minute = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("minute"))
                val second = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("second"))

                var isClieked = false
                val cursor_like : Cursor
                cursor_like = sqlitedb.rawQuery("SELECT isClicked FROM clickLike WHERE user_id = '${user_id}' AND post_id = ${post_id}", null)
                if(!cursor_like.moveToNext()){
                    sqlitedb.execSQL("INSERT INTO clickLike VALUES ('${user_id}', ${post_id}, 0);")
                }else{
                    if(cursor_like.getInt(cursor_like.getColumnIndexOrThrow("isClicked"))==1){
                        isClieked = true
                    }
                }

                val item = Post(profile, follow_id, post_id, picture, like, comment, date, hour, minute, second, isClieked)
                itemlist.add(item)
                //itemList에 친구의 피드 요소들을 더해 item을 늘림
            }
            cursor_post.close()
        }
        cursor_follow.close()

        itemlist.sortByDescending { it.post_id }
        val rv_adapter = PostAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        //어댑터와 리사이클러뷰 갱신

        rvHome.adapter=rv_adapter
        rvHome.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //어댑터를 연결해 레이아웃 매니저를 설정(리사이클러뷰 설정 완료)

        checkIfRecyclerViewIsEmpty(rv_adapter)
        //리사이클러뷰에 아이템 없을 경우 텍스트, 이미지 보이도록 하는 메서드 호출

        sqlitedb.close()
        dbManager.close()
    }

    //리사이클러뷰 아이템x 메서드
    private fun checkIfRecyclerViewIsEmpty(adapter: RecyclerView.Adapter<*>){
        if(adapter.itemCount == 0){
            emptyViewH.visibility = View.VISIBLE
            emptyImageH.visibility = View.VISIBLE
        }else{
            emptyViewH.visibility = View.GONE
            emptyImageH.visibility = View.GONE
        }
    }

    //하단 네비게이션바 기능 추가
    fun setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.home -> {
////                    val intent = Intent(this, Home::class.java)
////                    startActivity(intent)
//                    true
//                }

                R.id.timer -> {
                    val intent = Intent(this, Timer::class.java)
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