package com.example.team24app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var btnupload : ImageButton

    var time = 0
    private var timerTask: Timer? = null
    private var isRunning=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        tvdate = findViewById<TextView>(R.id.tvDate)
        tvhour = findViewById<TextView>(R.id.tvHours)
        tvmin = findViewById<TextView>(R.id.tvMinutes)
        tvsec = findViewById<TextView>(R.id.tvSeconds)
        btnss = findViewById<Button>(R.id.startStopButton)
        btnupload = findViewById<ImageButton>(R.id.btnTimeShare)

        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", java.util.Locale.KOREA)

        tvdate.text = dateFormat.format(now)
        //오늘의 날짜 설정

        btnss.setOnClickListener {
            //버튼 클릭 시 타이머 시작/멈춤
            isRunning=!isRunning

            if(isRunning){
                start()
            }else{
                pause()
            }
        }

        btnupload.setOnClickListener {
            val intent = Intent(this, UploadPost::class.java)
            intent.putExtra("date", tvdate.text.toString())
            intent.putExtra("hour", tvhour.text.toString().toInt())
            intent.putExtra("minute", tvmin.text.toString().toInt())
            intent.putExtra("hour", tvsec.text.toString().toInt())
            startActivity(intent)
        }
    }

    private fun start(){
        btnss.text = getString(R.string.stop)
        timerTask = timer(period=1000){
            time++

            val hour = (time/60)/60
            val min = (time/60)%60
            val sec = time%60
            //시분초 설정

            runOnUiThread {
                tvhour.text="$hour"
                tvmin.text="$min"
                tvsec.text="$sec"
            }
        }
    }

    private fun pause(){
        btnss.text=getString(R.string.start)
        timerTask?.cancel()
    }
}