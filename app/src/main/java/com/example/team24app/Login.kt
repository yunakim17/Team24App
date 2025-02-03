package com.example.team24app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    lateinit var edtLoginId: EditText
    lateinit var edtLoginPw: EditText
    lateinit var btnLogin: Button
    var DB: DBManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        DB = DBManager(this, "appDB", null, 1)

        edtLoginId = findViewById(R.id.edtLoginId)
        edtLoginPw = findViewById(R.id.edtLoginPw)
        btnLogin = findViewById(R.id.btnLogin)

        // 저장된 아이디 불러오기
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedUserId = sharedPreferences.getString("saved_user_id", "")
        edtLoginId.setText(savedUserId)  // 아이디 입력 필드에 자동 입력

        btnLogin.setOnClickListener {
            val user = edtLoginId.text.toString()
            val pass = edtLoginPw.text.toString()

            // 빈칸 제출시 예외 처리
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val checkUserpass = DB!!.checkUserpass(user, pass)
                if (checkUserpass) {
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                    // 로그인 아이디 저장
                    val editor = sharedPreferences.edit()
                    editor.putString("saved_user_id", user)
                    editor.apply()

                    //UserId.clearUserId(this) // 기존 로그인 정보 초기화
                    UserId.saveUserId(this, user) // 로그인한 유저 정보만 저장

                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}