package com.example.team24app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClickFeed : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_feed)

        ivProfile = findViewById<ImageView>(R.id.ivProfilePic)
        tvName = findViewById<TextView>(R.id.tvUserName)
        ivPost = findViewById<ImageView>(R.id.ivPostImg)
        btnLike = findViewById<ImageButton>(R.id.btnLike)
        tvLike = findViewById<TextView>(R.id.tvLikes)
        tvDescrip = findViewById<TextView>(R.id.tvPostDescription)
        tvDate = findViewById<TextView>(R.id.tvDate)
        tvHour = findViewById<TextView>(R.id.tvHours)
        tvMin = findViewById<TextView>(R.id.tvMinutes)
        tvSec = findViewById<TextView>(R.id.tvSeconds)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        val intent = intent
        val post_id = intent.getIntExtra("post_id", 0)
        //인텐트로 포스트id 가져옴

        val cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT * FROM post WHERE post_id = '"+post_id+"';", null)
        //post_id로 포스트 값 가져옴

        val user_id = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("user_id"))
        tvName.text = user_id
        //user_id 별도로 먼저 추출

        val cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile FROM user WHERE user_id = '"+ user_id +"';", null)
        ivProfile.setImageResource(cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("profile")))
        cursor_feed.close()
        //user_id를 통해 user 테이블에 접근하여 프로필 가져옴

        ivPost.setImageResource(cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("picture")))
        var like = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("num_like"))
        tvLike.text = "좋아요 " + like
        tvDescrip.text = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("comment"))
        tvDate.text = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("date"))
        tvHour.text = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("hour")).toString()
        tvMin.text = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("minute")).toString()
        tvSec.text = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("second")).toString()
        cursor_feed.close()
        //intent로 받아온 피드 정보를 입력

        btnLike.setOnClickListener {
            like++
            sqlitedb.execSQL("UPDATE post SET num_like = "+like+" WHERE post_id = '"+post_id+"';")
            tvLike.setText("좋아요 $like")
            btnLike.isEnabled=false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }
}