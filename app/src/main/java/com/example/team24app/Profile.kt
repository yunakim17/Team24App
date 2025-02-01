package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Profile : AppCompatActivity() {
    lateinit var tvName : TextView
    lateinit var ivProfile : ImageView
    lateinit var friend : TextView
    lateinit var tvDescrip : TextView
    lateinit var btnProfile : Button
    lateinit var btnUpload : Button
    lateinit var rvPost : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById<TextView>(R.id.tvUserName)
        ivProfile = findViewById<ImageView>(R.id.ivProfileImage)
        friend = findViewById<TextView>(R.id.tvFriendCount)
        tvDescrip = findViewById<TextView>(R.id.tvDescription)
        btnProfile = findViewById<Button>(R.id.btnEdtProfile)
        btnUpload = findViewById<Button>(R.id.btnPostUpload)
        rvPost = findViewById<RecyclerView>(R.id.rvPosts)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase
        val itemlist = ArrayList<Feed_Square>()
        val user_id = "tmp"
        //현재 사용자의 id(전역 추가시 추가)

        val cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile, num_friend, intro FROM user WHERE user_id = '"+user_id+"';", null)

        tvName.text = user_id
        ivProfile.setImageResource(cursor_user.getInt(cursor_user.getColumnIndexOrThrow("profile")))
        friend.text = cursor_user.getInt(cursor_user.getColumnIndexOrThrow("num_friend")).toString()
        tvDescrip.text = cursor_user.getString(cursor_user.getColumnIndexOrThrow("intro"))
        cursor_user.close()

        val cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT post_id, picture FROM post WHERE user_id = '"+user_id+"';", null)

        while (cursor_feed.moveToNext()){
            val post_id = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("post_id"))
            val picture = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("picture"))
            val item = Feed_Square(post_id, picture)
            itemlist.add(item)
        }
        cursor_feed.close()
        sqlitedb.close()
        dbManager.close()

        val rv_adapter = FeedAdapter(itemlist, this)
        rv_adapter.notifyDataSetChanged()
        rvPost.adapter=rv_adapter
        rvPost.layoutManager= GridLayoutManager(this, 3)
        //리사이클러뷰 어댑터 연결 완료

        btnProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
            //프로필 편집 화면으로 전환
        }
        btnUpload.setOnClickListener {
            val intent = Intent(this, UploadPost::class.java)
            startActivity(intent)
        }
    }
}