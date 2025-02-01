package com.example.team24app

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

        // 앱 실행 시 자동 로그인 확인
        UserId.loadUserId(this)
        if (UserId.userId != null) {
            Toast.makeText(this, "자동 로그인: ${UserId.userId}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

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

                    // 로그인한 user_id 전역 저장 (자동 로그인 기능)
                    UserId.saveUserId(this, user)

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