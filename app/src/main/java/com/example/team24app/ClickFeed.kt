package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClickFeed : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var ivProfile : ImageView
    lateinit var tvName : TextView
    lateinit var ivPost : ImageView
    lateinit var btnLike : ImageButton
    lateinit var tvLike : TextView
    lateinit var tvDescrip : TextView
    lateinit var tvDate : TextView
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    //하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_feed)

        btnBack = findViewById(R.id.btnBack)
        ivProfile = findViewById(R.id.ivProfilePic)
        tvName = findViewById(R.id.tvUserName)
        ivPost = findViewById(R.id.ivPostImg)
        btnLike = findViewById(R.id.btnLike)
        tvLike = findViewById(R.id.tvLikes)
        tvDescrip = findViewById(R.id.tvPostDescription)
        tvDate = findViewById(R.id.tvDate)
        tvHour = findViewById(R.id.tvHours)
        tvMin = findViewById(R.id.tvMinutes)
        tvSec = findViewById(R.id.tvSeconds)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        //하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        val intent = intent
        val post_id = intent.getIntExtra("post_id", 0)
        var like = 0
        //인텐트로 포스트id 가져옴

        val cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT * FROM post WHERE post_id = '"+post_id+"';", null)
        //post_id로 포스트 값 가져옴

        if(cursor_feed.moveToNext()){
            val user_id = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("user_id"))
            tvName.text = user_id
            //user_id 별도로 먼저 추출

            val cursor_user : Cursor
            cursor_user = sqlitedb.rawQuery("SELECT profile FROM user WHERE user_id = '"+ user_id +"';", null)
            //user_id로 프로필 가져옴

            if (cursor_user.moveToNext()){
                val profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
                if(profile != "tmp"){
                    val uri_profile = Uri.parse(profile)
                    ivProfile.setImageURI(uri_profile)
                }else{
                    ivProfile.setImageResource(R.drawable.img)
                }
            }
            cursor_user.close()

            val uri_picture = Uri.parse(cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("picture")))
            like = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("num_like"))

            ivPost.setImageURI(uri_picture)
            tvLike.text = "$like"
            tvDescrip.text = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("comment"))
            tvDate.text = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("date"))
            tvHour.text = "${cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("hour"))}"
            tvMin.text = "${cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("minute"))}"
            tvSec.text = "${cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("second"))}"
            //받아온 피드 정보를 입력
        }
        cursor_feed.close()

        btnBack.setOnClickListener {
            //뒤로가기 버튼
            onBackPressedDispatcher.onBackPressed()
        }

        btnLike.setOnClickListener {
            //좋아요 버튼
            like++
            sqlitedb.execSQL("UPDATE post SET num_like = "+like+" WHERE post_id = '"+post_id+"';")
            tvLike.text = "$like"
            btnLike.isEnabled=false
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