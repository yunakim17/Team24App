package com.example.team24app

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClickFeed : AppCompatActivity() {
    lateinit var ivProfile : ImageView
    lateinit var tvName : TextView
    lateinit var ivPost : ImageView
    lateinit var btnLike : Button
    lateinit var tvLike : TextView
    lateinit var tvDescrip : TextView
    lateinit var date : TextView
    lateinit var hour : TextView
    lateinit var minute : TextView
    lateinit var secound : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click_feed)

        ivProfile = findViewById<ImageView>(R.id.ivProfilePic)
        tvName = findViewById<TextView>(R.id.tvUserName)
        ivPost = findViewById<ImageView>(R.id.ivPostImg)
        btnLike = findViewById<Button>(R.id.btnLike)
        tvLike = findViewById<TextView>(R.id.tvLikes)
        tvDescrip = findViewById<TextView>(R.id.tvPostDescription)
        date = findViewById<TextView>(R.id.tvDate)
        hour = findViewById<TextView>(R.id.tvHours)
        minute = findViewById<TextView>(R.id.tvMinutes)
        secound = findViewById<TextView>(R.id.tvSeconds)

        val post_data = intent.getSerializableExtra("post") as Post
        ivProfile.setImageResource(post_data.profile)
        tvName.text = post_data.id
        ivPost.setImageResource(post_data.img)
        tvLike.text = post_data.like
        tvDescrip.text = post_data.comment
        //intent로 받아온 피드 정보를 입력

        btnLike.setOnClickListener {
            var num = tvLike.text.replace("[^0-9]".toRegex(), "").toInt()
            num++
            tvLike.setText("좋아요 $num")

        }
    }
}