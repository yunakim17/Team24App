package com.example.team24app

import android.widget.ImageView
import android.widget.TextView

//data class 상속은 추천되지 않아 따로 작성
data class Post(
    val userProfile : ImageView,
    val userId : TextView,
    val imgPost : ImageView,
    val numLike : TextView,
    val comment : TextView
)
