package com.example.team24app

import java.io.Serializable

//profile_list의 구성을 넣아함
data class User(
    val profile : String,
    val id : String,
) : Serializable
