package com.example.team24app

import java.io.Serializable

// 아이디 검색 리사이클러뷰에 사용하는 데이터 클래스
data class User(
    val profile : String,
    val id : String,
) : Serializable
