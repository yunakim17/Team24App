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
import java.io.File

// 포스트를 보는 화면
class ClickPost : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var ivProfile : ImageView
    lateinit var tvName : TextView
    lateinit var ivPicture : ImageView
    lateinit var btnLike : ImageButton
    lateinit var tvLike : TextView
    lateinit var tvDescrip : TextView
    lateinit var tvDate : TextView
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_post)

        btnBack = findViewById(R.id.btnBack)
        ivProfile = findViewById(R.id.ivProfilePic)
        tvName = findViewById(R.id.tvUserName)
        ivPicture = findViewById(R.id.ivPostImg)
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


        // 인텐트로 post_id 전달
        val intent = intent
        val post_id = intent.getIntExtra("post_id", 0)
        var like = 0

        val cursor_post : Cursor
        cursor_post = sqlitedb.rawQuery("SELECT * FROM post WHERE post_id = ?", arrayOf(post_id.toString()))
        //post_id로 포스트 값 가져옴

        if(cursor_post.moveToNext()){
            val owner_id = cursor_post.getString(cursor_post.getColumnIndexOrThrow("user_id"))
            tvName.text = owner_id
            //user_id 별도로 먼저 추출

            val cursor_user : Cursor
            cursor_user = sqlitedb.rawQuery("SELECT profile FROM user WHERE user_id = ?", arrayOf(owner_id))
            //user_id로 프로필 가져옴

            if (cursor_user.moveToNext()){
                val profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
                if(profile != "tmp"){
                    val uri_profile = Uri.fromFile(File(profile))
                    ivProfile.setImageURI(uri_profile)
                }else{
                    ivProfile.setImageResource(R.drawable.default_profile)
                }
            }
            cursor_user.close()

            val picture = cursor_post.getString(cursor_post.getColumnIndexOrThrow("picture"))
            val uri_picture = Uri.fromFile(File(picture))
            like = cursor_post.getInt(cursor_post.getColumnIndexOrThrow("num_like"))

            ivPicture.setImageURI(uri_picture)
            tvLike.text = "$like"
            tvDescrip.text = cursor_post.getString(cursor_post.getColumnIndexOrThrow("comment"))
            tvDate.text = cursor_post.getString(cursor_post.getColumnIndexOrThrow("date"))
            tvHour.text = "${cursor_post.getInt(cursor_post.getColumnIndexOrThrow("hour"))}"
            tvMin.text = "${cursor_post.getInt(cursor_post.getColumnIndexOrThrow("minute"))}"
            tvSec.text = "${cursor_post.getInt(cursor_post.getColumnIndexOrThrow("second"))}"
            //받아온 피드 정보를 입력
        }
        cursor_post.close()

        val user_id = UserId.userId
        var isClicked = false
        val cursor_like : Cursor
        cursor_like = sqlitedb.rawQuery("SELECT isClicked FROM clickLike WHERE user_id = ? AND post_id = ?", arrayOf(user_id, post_id.toString()))
        if(!cursor_like.moveToNext()){
            sqlitedb.execSQL("INSERT INTO clickLike VALUES (?, ?, 0)", arrayOf(user_id, post_id))
        }else{
            if(cursor_like.getInt(cursor_like.getColumnIndexOrThrow("isClicked"))==1){
                isClicked = true
                btnLike.setBackgroundResource(R.drawable.like_filled)

            }
        }
        cursor_like.close()

        // 좋아요 버튼
        btnLike.setOnClickListener {
            isClicked = !isClicked
            if(isClicked){
                like++
                sqlitedb.execSQL("UPDATE post SET num_like = ? WHERE post_id = ?", arrayOf(like, post_id))
                sqlitedb.execSQL("UPDATE clickLike SET isClicked = 1 WHERE user_id = ? AND post_id = ?", arrayOf(user_id, post_id))
                tvLike.text = "$like"
                btnLike.setBackgroundResource(R.drawable.like_filled)
            }else{
                like--
                sqlitedb.execSQL("UPDATE post SET num_like = ? WHERE post_id = ?", arrayOf(like, post_id))
                sqlitedb.execSQL("UPDATE clickLike SET isClicked = 0 WHERE user_id = ? AND post_id = ?", arrayOf(user_id, post_id))
                tvLike.text = "$like"
                btnLike.setBackgroundResource(R.drawable.like_btn)
            }
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // 피드에서 나갈 때, DB 종료
    override fun onStop() {
        super.onStop()
        sqlitedb.close()
        dbManager.close()
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
}