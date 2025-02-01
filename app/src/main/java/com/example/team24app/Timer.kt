package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.math.min

class Timer : AppCompatActivity() {
    lateinit var tvdate : TextView
    lateinit var tvhour : TextView
    lateinit var tvmin : TextView
    lateinit var tvsec : TextView
    lateinit var btnss : Button
    lateinit var btnReset : Button
    lateinit var btnupload : ImageButton
    val time_format = DecimalFormat("00")
    var hour = 0
    var minute = 0
    var second = 0

    var time = 0
    private var timerTask: Timer? = null
    private var isRunning=false

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

        val now = Date()
        val timerFormat = SimpleDateFormat("yyyy년 MM월 dd일", java.util.Locale.KOREA)
        val postFormat= SimpleDateFormat("yyyy/MM/dd", java.util.Locale.KOREA)
        Time.date = postFormat.format(now)
        tvdate.text = timerFormat.format(now)
        //오늘의 날짜 설정


        btnss.setOnClickListener {
            //버튼 클릭 시 타이머 시작/멈춤
            isRunning=!isRunning

            if(isRunning){
                //시작

                btnss.text = getString(R.string.stop)
                timerTask = timer(period=1000){
                    time++

                    hour = (time/60)/60
                    minute = (time/60)%60
                    second = time%60
                    //시분초 설정

                    runOnUiThread {
                        tvhour.text=time_format.format(hour)
                        tvmin.text=time_format.format(minute)
                        tvsec.text=time_format.format(second)
                    }
                }
            }else{
                //정지
                btnss.text=getString(R.string.start)
                timerTask?.cancel()
                Time.hour = time_format.format(hour).toInt()
                Time.minute = time_format.format(minute).toInt()
                Time.second = time_format.format(second).toInt()
            }
        }

        btnReset.setOnClickListener {
            //시간 초기화
            timerTask?.cancel()
            time = 0
            isRunning=false
            btnss.text=getString(R.string.start)
            Time.hour = 0
            Time.minute = 0
            Time.second = 0
            tvhour.text="00"
            tvmin.text="00"
            tvsec.text="00"
        }

        btnupload.setOnClickListener {
            //업로드 화면 전환
            timerTask?.cancel()
            isRunning=false
            btnss.text=getString(R.string.start)
            Time.hour = time_format.format(hour).toInt()
            Time.minute = time_format.format(minute).toInt()
            Time.second = time_format.format(second).toInt()
            val intent = Intent(this, UploadPost::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        timerTask?.cancel()
        isRunning=false
        btnss.text=getString(R.string.start)
        Time.hour = time_format.format(hour).toInt()
        Time.minute = time_format.format(minute).toInt()
        Time.second = time_format.format(second).toInt()
    }
}