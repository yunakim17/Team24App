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
        db!!.execSQL("CREATE TABLE post (post_id INTEGER NOT NULL PRIMARY KEY, user_id VARCHAR(10) NOT NULL, picture INTEGER NOT NULL, num_like INTEGER NOT NULL, comment TEXT, date VARCHAR(15) NOT NULL, hour INTEGER NOT NULL, minute INTEGER NOT NULL, second INTEGER NOT NULL, foreign key(user_id) references user (user_id))")
        db!!.execSQL("CREATE TABLE friend (from_id VARCHAR(10) NOT NULL, to_id VARCHAR(10) NOT NULL, PRIMARY KEY (from_id, to_id), foreign key(from_id) references user (user_id), foreign key(to_id) references user (user_id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}