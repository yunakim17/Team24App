package com.example.team24app

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AnotherProfile : AppCompatActivity() {
    lateinit var name : TextView
    lateinit var profile : ImageView
    lateinit var friend : TextView
    lateinit var description : TextView
    lateinit var btnAdd : Button
    lateinit var rvPost : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_profile)

        name = findViewById<TextView>(R.id.tvUserName)
        profile = findViewById<ImageView>(R.id.ivProfileImage)
        friend = findViewById<TextView>(R.id.tvFriendCount)
        description = findViewById<TextView>(R.id.tvDescription)
        btnAdd = findViewById<Button>(R.id.btnAddFriend)
        rvPost = findViewById<RecyclerView>(R.id.rvPosts)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase
        val itemlist = ArrayList<Feed_Square>()
        val user_id = "tmp"
        //현재 사용자의 id(전역 추가시 추가)

        val intent = intent
        val friend_id = intent.getStringExtra("user_id").toString()

        var cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile, num_friend, intro FROM user WHERE user_id = '"+friend_id+"';", null)

        name.text = friend_id
        profile.setImageResource(cursor_user.getInt(cursor_user.getColumnIndexOrThrow("profile")))
        friend.text = cursor_user.getInt(cursor_user.getColumnIndexOrThrow("num_friend")).toString()
        description.text = cursor_user.getString(cursor_user.getColumnIndexOrThrow("intro"))
        cursor_user.close()
        //테이블에서 상대 프로필 정보를 끌고옴

        val cursor_friend : Cursor
        cursor_friend = sqlitedb.rawQuery("SELECT * FROM friend WHERE from_id = '"+user_id+"' AND to_id = '"+friend_id+"';", null)

        if(cursor_friend.moveToNext()){
            //친구 관계라면 버튼 비활성화
            btnAdd.isEnabled=false
            btnAdd.text = getString(R.string.following)
        }
        cursor_friend.close()


        var cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT post_id, picture FROM post WHERE user_id = '"+friend_id+"';", null)

        while (cursor_feed.moveToNext()){
            val post_id = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("post_id"))
            val picture = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("picture"))
            val item = Feed_Square(post_id, picture)
            itemlist.add(item)
        }
        cursor_feed.close()

        val rv_adapter = FeedAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        rvPost.adapter=rv_adapter
        rvPost.layoutManager= GridLayoutManager(this, 3)

        btnAdd.setOnClickListener {
            sqlitedb.execSQL("INSERT INTO friend VALUES ('"+user_id+"', '"+friend_id+"');")
            btnAdd.text = getString(R.string.following)
            btnAdd.isEnabled=false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sqlitedb.close()
        dbManager.close()
    }
}