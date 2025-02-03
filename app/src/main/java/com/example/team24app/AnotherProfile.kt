package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class AnotherProfile : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var tvName : TextView
    lateinit var ivProfile : ImageView
    lateinit var tvFollow : TextView
    lateinit var tvDesc : TextView
    lateinit var btnAdd : Button
    lateinit var rvPost : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    //하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_profile)

        btnBack = findViewById(R.id.btnBack)
        tvName = findViewById(R.id.tvUserName)
        ivProfile = findViewById(R.id.ivProfileImage)
        tvFollow = findViewById(R.id.tvFollowCount)
        tvDesc = findViewById(R.id.tvDescription)
        btnAdd = findViewById(R.id.btnAddFollow)
        rvPost = findViewById(R.id.rvPosts)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        //하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        val itemlist = ArrayList<Feed_Square>()
        val user_id = UserId.userId
        var num_follow = 0

        val intent = intent
        val other_id = intent.getStringExtra("user_id")
        //다른 유저의 아이디 인텐트로 받아옴

        var cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile, num_follow, intro FROM user WHERE user_id = '"+other_id+"';", null)
        //아이디를 이용해 해당 유저의 데이터 가져옴

        if(cursor_user.moveToNext()){
            val profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
            if(profile != "tmp"){
                val uri = Uri.fromFile(File(profile))
                ivProfile.setImageURI(uri)
            }else{
                ivProfile.setImageResource(R.drawable.img)
            }

            num_follow = cursor_user.getInt(cursor_user.getColumnIndexOrThrow("num_follow"))

            tvName.text = other_id
            tvFollow.text = "${num_follow}"
            tvDesc.text = cursor_user.getString(cursor_user.getColumnIndexOrThrow("intro"))
            //테이블에서 상대 프로필 정보를 끌고옴
        }
        cursor_user.close()

        val cursor_follow : Cursor
        cursor_follow = sqlitedb.rawQuery("SELECT * FROM follow WHERE from_id = '"+user_id+"' AND to_id = '"+other_id+"';", null)
        //친구 관계 확인

        if(cursor_follow.moveToNext()){
            btnAdd.isEnabled=false
            btnAdd.text = getString(R.string.following)
            //친구 관계라면 버튼 비활성화
        }
        cursor_follow.close()

        var cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT post_id, picture FROM post WHERE user_id = '"+other_id+"';", null)
        //사각형 피드 데이터 가져옴

        while (cursor_feed.moveToNext()){
            val post_id = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("post_id"))
            val picture = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("picture"))
            val item = Feed_Square(post_id, picture)
            itemlist.add(item)
        }
        cursor_feed.close()

        val rv_adapter = FeedAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        rvPost.adapter=rv_adapter
        rvPost.layoutManager= GridLayoutManager(this, 3)
        //리사이클러뷰 적용

        btnBack.setOnClickListener {
            //뒤로가기 버튼
            onBackPressedDispatcher.onBackPressed()
        }

        btnAdd.setOnClickListener {
            //친구추가 버튼
            num_follow++
            tvFollow.text = "${num_follow}"
            btnAdd.text = getString(R.string.following)
            btnAdd.isEnabled=false
            sqlitedb.execSQL("INSERT INTO follow VALUES ('"+user_id+"', '"+other_id+"');")
            sqlitedb.execSQL("UPDATE user SET num_follow = "+num_follow+" WHERE user_id = '"+other_id+"';")
        }
    }

    //하단 네비게이션바 기능 추가
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

    override fun onStop() {
        super.onStop()
        sqlitedb.close()
        dbManager.close()
    }
}