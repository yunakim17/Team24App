package com.example.team24app

import android.content.Intent
import java.util.regex.Pattern
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup : AppCompatActivity() {

    var DB: DBManager? = null
    lateinit var edtSignupId: EditText
    lateinit var btnIdCheck: Button
    var IdCheck: Boolean = false
    lateinit var edtSignupEmail: EditText
    lateinit var edtSignupPwd: EditText
    lateinit var edtPwdCheck: EditText
    lateinit var btnRegister: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        DB = DBManager(this, "Login", null, 1)
        edtSignupId = findViewById(R.id.edtSignupId)
        edtSignupEmail = findViewById(R.id.edtSignupEmail)
        edtSignupPwd = findViewById(R.id.edtSignupPwd)
        edtPwdCheck = findViewById(R.id.edtPwdCheck)
        btnRegister = findViewById(R.id.btnRegister)
        btnIdCheck = findViewById(R.id.btnIdCheck)


        // 아이디 중복 학인 버튼
        btnIdCheck.setOnClickListener {
            val user = edtSignupId.text.toString()
            val idPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{6,15}$"

            if (user == "") {
                Toast.makeText(
                    this@Signup,
                    "아이디를 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (Pattern.matches(idPattern, user)) {
                    val checkUsername = DB!!.checkUser(user)
                    if (checkUsername == false) {
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

        // 정보 입력 후 회원가입 버튼 클릭
        btnRegister.setOnClickListener {
            val user = edtSignupId.text.toString()
            val pass = edtSignupPwd.text.toString()
            val repass = edtPwdCheck.text.toString()
            val email = edtSignupEmail.text.toString()
            val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]{8,15}$"
            // 입력란이 비었을 경우
            if (user == "" || pass == "" || repass == "" || email == "") Toast.makeText(
                this@Signup,
                "회원정보를 모두 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            else {
                // 아이디 중복 확인이 완료 되었을 경우
                if (IdCheck == true) {
                    // 비밀번호 형식이 맞을 경우
                    if (Pattern.matches(pwPattern, pass)) {
                        // 비밀번호 재확인 성공
                        if (pass == repass) {
                            val insert = DB!!.insertUser(user, pass, email)
                            // 가입 성공
                            if (insert == true) {
                                Toast.makeText(
                                    this@Signup,
                                    "가입되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(applicationContext, Home::class.java)
                                startActivity(intent)
                            }
                            // 가입 실패
                            else {
                                Toast.makeText(
                                    this@Signup,
                                    "가입에 실패하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        // 비밀번호 재확인 실패
                        else {
                            Toast.makeText(
                                this@Signup,
                                "비밀번호가 일치하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    // 비밀번호 형식이 맞지 않을 경우
                    else {
                        Toast.makeText(
                            this@Signup,
                            "비밀번호 형식이 옳지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // 아이디 중복확인 실패
                else {
                    Toast.makeText(this@Signup, "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }
}