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

// 타이머 화면
class Timer : AppCompatActivity() {
    lateinit var tvDate : TextView
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var btnTimer : Button
    lateinit var btnReset : Button
    lateinit var btnUpload : ImageButton
    lateinit var bottomNavigationView: BottomNavigationView
    val timeFormat = DecimalFormat("00")
    var timerTask: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        tvDate = findViewById(R.id.tvDate)
        tvHour = findViewById(R.id.tvHours)
        tvMin = findViewById(R.id.tvMinutes)
        tvSec = findViewById(R.id.tvSeconds)
        btnTimer = findViewById(R.id.startStopButton)
        btnReset = findViewById(R.id.resetButton)
        btnUpload = findViewById(R.id.btnTimeShare)

        // 네비게이션 바
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        // 오늘의 날짜 설정
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", java.util.Locale.KOREA)
        tvDate.text = dateFormat.format(now)

        // 타이머의 데이터가 남은채로 다른 화면으로 옮겼다면 기록을 받아옴
        if(Time.backRunning == true){
            tvHour.text = timeFormat.format(Time.hour)
            tvMin.text = timeFormat.format(Time.minute)
            tvSec.text = timeFormat.format(Time.second)

            if(Time.isRunning == true){
                // 타이머가 작동중이었다면 작동 중이던 시간을 받아와 다시 실행
                Time.timerTask?.cancel()
                Start()
            }
        }

        // 버튼 클릭 시 타이머 시작/멈춤
        btnTimer.setOnClickListener {
            Time.isRunning=!Time.isRunning

            if(Time.isRunning){
                Start()
            }else{
                Stop()
            }
        }

        //시간 초기화
        btnReset.setOnClickListener {
            Stop()
            Time.isRunning=false
            Time.backRunning=false
            Time.time = 0
            Time.hour = 0
            Time.minute = 0
            Time.second = 0
            tvHour.text="00"
            tvMin.text="00"
            tvSec.text="00"
        }

        // 업로드 화면 버튼
        btnUpload.setOnClickListener {
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

                R.id.profile -> {
                    val intent = Intent(this, Profile::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }

        }
    }

    // 타이머 시작
    fun Start() {
        btnTimer.text = getString(R.string.stop)
        timerTask = timer(period = 1000) {
            Time.time++

            // 시분초 설정
            Time.hour = (Time.time / 60) / 60
            Time.minute = (Time.time / 60) % 60
            Time.second = Time.time % 60

            // 화면 설정
            runOnUiThread {
                tvHour.text = timeFormat.format(Time.hour)
                tvMin.text = timeFormat.format(Time.minute)
                tvSec.text = timeFormat.format(Time.second)

            }
        }
    }

    // 타이머 정지
    fun Stop(){
        btnTimer.text=getString(R.string.start)
        timerTask?.cancel()
    }

    // 화면을 옮겼을 때, 타이머의 상태 확인
    override fun onStop() {
        super.onStop()

        // 타이머의 기록이 존재함(기록 백업)
        if(Time.time != 0){
            Time.backRunning = true
            Time.hour = (Time.time / 60) / 60
            Time.minute = (Time.time / 60) % 60
            Time.second = Time.time % 60

            // 타이머가 작동 중이었음(백그라운드에서 시간이 흐르도록 설정)
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