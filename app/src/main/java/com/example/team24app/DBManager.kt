package com.example.team24app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DBManager(context: Context,
                name: String?,
                factory: SQLiteDatabase.CursorFactory?,
                version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE user (user_id VARCHAR(10) NOT NULL PRIMARY KEY, email VARCHAR(40) NOT NULL, password VARCHAR(20) NOT NULL, profile INTEGER, num_friend INTEGER, intro TEXT)")
        db!!.execSQL("CREATE TABLE feed (feed_id INTEGER NOT NULL PRIMARY KEY, user_id VARCHAR(10) NOT NULL, picture INTEGER NOT NULL, num_like INTEGER NOT NULL, comment TEXT, time INTEGER NOT NULL, foreign key(user_id) references user (user_id))")
        db!!.execSQL("CREATE TABLE friend (friend_id INTEGER NOT NULL, from_id VARCHAR(10) NOT NULL, to_id VARCHAR(10) NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop Table if exists user")
    }

    fun insertUser(user_id: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("user_id", user_id)
        contentValues.put("email", email)
        contentValues.put("password", password)
        val result = db.insert("user", null, contentValues)
        db.close()
        return result != -1L // return if (result == -1L) false else true 삽입 성공 여부 반환
    }

    // 사용자 아이디가 존재하면 true, 존재하지 않는다면 false
    fun checkUser(id: String?): Boolean {
        val db = this.readableDatabase
        var res = true
        val cursor = db.rawQuery("Select * from user where id =?", arrayOf(id))
        if (cursor.count <= 0) res = false
        return res
    }

    // 해당 id, password가 있는지 확인, 없을 경우 false)
    fun checkUserpass(id: String, password: String) : Boolean {
        val db = this.writableDatabase
        var res = true
        val cursor = db.rawQuery(
            "Select * from user where id = ? and password = ?",
            arrayOf(id, password)
        )
        if (cursor.count <= 0) res = false
        return res
    }


}