package com.example.team24app

import java.io.Serializable

//data class 상속은 추천되지 않아 따로 작성
data class Post(
    val profile : Int,
    val id : String,
    val img : Int,
    val like : String,
    val comment : String
) : Serializable
