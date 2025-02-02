package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import kotlin.concurrent.timer

class Timer : AppCompatActivity() {
    lateinit var tvdate : TextView
    lateinit var tvhour : TextView
    lateinit var tvmin : TextView
    lateinit var tvsec : TextView
    lateinit var btnss : Button
    lateinit var btnReset : Button
    lateinit var btnupload : ImageButton
    lateinit var bottomNavigationView: BottomNavigationView
    val time_format = DecimalFormat("00")

    private var timerTask: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        tvdate = findViewById(R.id.tvDate)
        tvhour = findViewById(R.id.tvHours)
        tvmin = findViewById(R.id.tvMinutes)
        tvsec = findViewById(R.id.tvSeconds)
        btnss = findViewById(R.id.startStopButton)
        btnReset = findViewById(R.id.resetButton)
        btnupload = findViewById(R.id.btnTimeShare)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        val now = Date()
        val timerFormat = SimpleDateFormat("yyyy년 MM월 dd일", java.util.Locale.KOREA)
        val postFormat= SimpleDateFormat("yyyy/MM/dd", java.util.Locale.KOREA)
        Time.date = postFormat.format(now)
        tvdate.text = timerFormat.format(now)
        //오늘의 날짜 설정

        if(Time.backRunning == true){
            //타이머의 기록이 존재함
            tvhour.text = time_format.format(Time.hour)
            tvmin.text = time_format.format(Time.minute)
            tvsec.text = time_format.format(Time.second)

            if(Time.isRunning == true){
                //타이머가 작동중이었음
                Time.timerTask?.cancel()
                Start()
            }
        }

        btnss.setOnClickListener {
            //버튼 클릭 시 타이머 시작/멈춤
            Time.isRunning=!Time.isRunning

            if(Time.isRunning){
                //시작
                Start()
            }else{
                //정지
                Stop()
            }
        }

        btnReset.setOnClickListener {
            //시간 초기화
            Stop()
            Time.isRunning=false
            Time.backRunning=false

            Time.time = 0
            Time.hour = 0
            Time.minute = 0
            Time.second = 0
            tvhour.text="00"
            tvmin.text="00"
            tvsec.text="00"
        }

        btnupload.setOnClickListener {
            //업로드 화면 전환
            Stop()
            Time.isRunning=false
            val intent = Intent(this, UploadPost::class.java)
            startActivity(intent)
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

//                R.id.timer -> {
////                    val intent = Intent(this, com.example.team24app.Timer::class.java)
////                    startActivity(intent)
//                    true
//                }

                R.id.profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }

                else -> false

            }

        }
    }
    fun Start() {
        btnss.text = getString(R.string.stop)
        timerTask = timer(period = 1000) {
            //time++
            Time.time++
            Time.hour = (Time.time / 60) / 60
            Time.minute = (Time.time / 60) % 60
            Time.second = Time.time % 60
            //시분초 설정

            runOnUiThread {
                tvhour.text = time_format.format(Time.hour)
                tvmin.text = time_format.format(Time.minute)
                tvsec.text = time_format.format(Time.second)

            }
        }
    }

    fun Stop(){
        btnss.text=getString(R.string.start)
        timerTask?.cancel()
    }

    override fun onStop() {
        super.onStop()
        if(Time.time != 0){
            Time.backRunning = true
            //Time.time = time
            Time.hour = (Time.time / 60) / 60
            Time.minute = (Time.time / 60) % 60
            Time.second = Time.time % 60
            if(Time.isRunning == true){
                timerTask?.cancel()
                Time.timerTask = timer(period = 1000){
                    Time.time++
                    Time.hour = (Time.time / 60) / 60
                    Time.minute = (Time.time / 60) % 60
                    Time.second = Time.time % 60
                }
            }
        }
    }
}