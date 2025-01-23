package com.example.team24app

import android.os.Bundle
import android.widget.Button
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
    lateinit var ssbtn : Button

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
        ssbtn = findViewById<Button>(R.id.startStopButton)

        var now = Date()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        tvdate.text = dateFormat.format(now)

        ssbtn.setOnClickListener {
            isRunning=!isRunning

            if(isRunning){
                start()
            }else{
                pause()
            }
        }

    }

    private fun start(){
        ssbtn.text = getString(R.string.stop)
        timerTask = timer(period=1000){
            time++

            val hour = (time/60)/60
            val min = (time/60)%60
            val sec = time%60

            runOnUiThread {
                tvhour.text="$hour"
                tvmin.text="$min"
                tvsec.text="$sec"
            }
        }
    }

    private fun pause(){
        ssbtn.text=getString(R.string.start)
        timerTask?.cancel()
    }
}