package com.example.team24app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditProfile : AppCompatActivity() {
    lateinit var tvName : TextView
    lateinit var tvDescrip : TextView
    lateinit var btnSave : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        tvName = findViewById<TextView>(R.id.tvUserName)
        tvDescrip = findViewById<TextView>(R.id.edtDescription)
        btnSave = findViewById<Button>(R.id.btnSaveProfile)

        btnSave.setOnClickListener {
            //테이블에 수정 소개 문구와 이미지 경로 수정 예정
        }
    }
}