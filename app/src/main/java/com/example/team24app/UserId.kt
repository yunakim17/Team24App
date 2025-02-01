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

    // 저장된 user_id 불러오기 (앱 실행 시 자동 로그인 가능)
    fun loadUserId(context: Context) {
        val dbHelper = UserIdDBHelper(context)
        userId = dbHelper.getUser()
    }

    // 로그아웃 시 user_id 삭제 (로그아웃 후 자동로그인이 되지 않게함, DB에는 정보 남아있음)
    fun clearUserId(context: Context) {
        userId = null
        val dbHelper = UserIdDBHelper(context)
        dbHelper.clearUser()
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

    // 저장된 user_id 불러오기
    fun getUser(): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT user_id FROM logged_user LIMIT 1", null)
        var userId: String? = null

        if (cursor.moveToFirst()) {
            userId = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return userId
    }

    // 로그아웃 시 user_id 삭제
    fun clearUser() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM logged_user")
        db.close()
    }
}
