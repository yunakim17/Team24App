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
        db!!.execSQL("CREATE TABLE user (user_id VARCHAR(10) NOT NULL PRIMARY KEY, email VARCHAR(40) NOT NULL, password VARCHAR(20) NOT NULL, profile INTEGER, num_friend INTEGER, intro TEXT)")
        db!!.execSQL("CREATE TABLE feed (feed_id INTEGER NOT NULL PRIMARY KEY, user_id VARCHAR(10) NOT NULL, picture INTEGER NOT NULL, num_like INTEGER NOT NULL, comment TEXT, time INTEGER NOT NULL, foreign key(user_id) references user (user_id))")
        db!!.execSQL("CREATE TABLE friend (friend_id INTEGER NOT NULL, from_id VARCHAR(10) NOT NULL, to_id VARCHAR(10) NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}