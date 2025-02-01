package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    var DB: DBManager? = null
    lateinit var edtSignupId: EditText
    lateinit var btnIdCheck: Button
    var IdCheck: Boolean = false
    lateinit var edtSignupEmail: EditText
    lateinit var edtSignupPwd: EditText
    lateinit var edtPwdCheck: EditText
    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        DB = DBManager(this, "appDB", null, 1)
        edtSignupId = findViewById(R.id.edtSignupId)
        edtSignupEmail = findViewById(R.id.edtSignupEmail)
        edtSignupPwd = findViewById(R.id.edtSignupPwd)
        edtPwdCheck = findViewById(R.id.edtPwdCheck)
        btnRegister = findViewById(R.id.btnRegister)
        btnIdCheck = findViewById(R.id.btnIdCheck)

        // 아이디 중복 확인
        btnIdCheck.setOnClickListener {
            val user = edtSignupId.text.toString()
            val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{6,15}$"

            if (user.isEmpty()) {
                Toast.makeText(this@Signup, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (Pattern.matches(idPattern, user)) {
                    val checkUsername = DB!!.checkUser(user)
                    if (!checkUsername) {
                        IdCheck = true
                        Toast.makeText(this@Signup, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Signup, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup, "아이디 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 회원가입 버튼 클릭
        btnRegister.setOnClickListener {
            val user = edtSignupId.text.toString()
            val pass = edtSignupPwd.text.toString()
            val repass = edtPwdCheck.text.toString()
            val email = edtSignupEmail.text.toString()
            val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,15}$"

            if (user.isEmpty() || pass.isEmpty() || repass.isEmpty() || email.isEmpty()) {
                Toast.makeText(this@Signup, "회원정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (IdCheck) {
                    if (Pattern.matches(pwPattern, pass)) {
                        if (pass == repass) {
                            val insert = DB!!.insertUser(user, email, pass)
                            if (insert) {
                                Toast.makeText(this@Signup, "가입되었습니다.", Toast.LENGTH_SHORT).show()

                                // ✅ 회원가입 후 자동 로그인 설정
                                UserId.saveUserId(this, user)

                                val intent = Intent(applicationContext, Home::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@Signup, "가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@Signup, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Signup, "비밀번호 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}