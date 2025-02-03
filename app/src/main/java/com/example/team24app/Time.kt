package com.example.team24app

import java.util.Timer

object Time {
    var hour : Int = 0
    var minute : Int = 0
    var second : Int = 0
    var time = 0
    var backRunning = false
    var isRunning = false
    var timerTask : Timer? = null
}