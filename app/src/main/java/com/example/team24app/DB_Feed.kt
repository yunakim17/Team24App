package com.example.team24app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB_Feed(context: Context,
              name: String?,
              factory: SQLiteDatabase.CursorFactory?,
              version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE feed (feed_id VARCHER(10) PRIMARY KEY, user_id INTEGER, picture VARCHER(10), heart INTEGER, comment text, foreign key(user_id) references user (user_id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}