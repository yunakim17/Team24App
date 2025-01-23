package com.example.team24app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(context: Context,
                name: String?,
                factory: SQLiteDatabase.CursorFactory?,
                version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE user (user_id VARCHAR(10) PRIMARY KEY, email VARCHAR(40), password VARCHAR(20), profile VARCHAR(10), friend_num INTEGER, intro TEXT)")
        db!!.execSQL("CREATE TABLE feed (feed_id VARCHAR(10) PRIMARY KEY, user_id VARCHAR(10), picture VARCHAR(10), heart INTEGER, comment text, time INTEGER, foreign key(user_id) references user (user_id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}