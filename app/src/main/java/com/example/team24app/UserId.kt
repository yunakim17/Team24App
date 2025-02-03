package com.example.team24app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

object UserId {
    var userId: String? = null  // 현재 로그인한 사용자 ID (전역 변수)

    // user_id 저장 (앱 종료 후에도 유지)
    fun saveUserId(context: Context, id: String) {
        userId = id
        val dbHelper = UserIdDBHelper(context)
        dbHelper.saveUser(id)
    }

}

// user_id 저장 & 불러오기 (로그인 유지 기능)
class UserIdDBHelper(context: Context) :
    SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS logged_user (user_id TEXT PRIMARY KEY)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS logged_user")
        onCreate(db)
    }

    // user_id 저장
    fun saveUser(user_id: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM logged_user") // 기존 데이터 삭제 (항상 1개만 유지)
        val contentValues = ContentValues()
        contentValues.put("user_id", user_id)
        db.insert("logged_user", null, contentValues)
        db.close()
    }

}
