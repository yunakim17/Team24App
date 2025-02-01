package com.example.team24app

import java.io.Serializable

//data class 상속은 추천되지 않아 따로 작성
data class Post(
    val profile : Int,
    val id : String,
    val post_id : Int,
    val picture : Int,
    val like : Int,
    val comment : String,
    val date : String,
    val hour : Int,
    val minute : Int,
    val second : Int
) : Serializable
