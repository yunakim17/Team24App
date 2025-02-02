package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

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

    //하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //btnBack = findViewById(R.id.btnBack)
        tvName = findViewById(R.id.tvUserName)
        ivProfile = findViewById(R.id.ivProfileImage)
        friend = findViewById(R.id.tvFriendCount)
        tvDescrip = findViewById(R.id.tvDescription)
        btnProfile = findViewById(R.id.btnEdtProfile)
        btnUpload = findViewById(R.id.btnPostUpload)
        rvPost = findViewById(R.id.rvPosts)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        //하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()

        val itemlist = ArrayList<Feed_Square>()

        val user_id = UserId.userId
        tvName.text = user_id

        val cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile, num_friend, intro FROM user WHERE user_id = '${user_id}';", null)
        //user_id로 프로필 데이터를 가져옴

        if(cursor_user.moveToNext()){
            //val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            //this.contentResolver.takePersistableUriPermission(uri, takeFlags)
            val profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
            if(profile != "tmp"){
                val uri = Uri.parse(profile)
                ivProfile.setImageURI(uri)
            }else{
                ivProfile.setImageResource(R.drawable.img)
            }

            friend.text = "${cursor_user.getInt(cursor_user.getColumnIndexOrThrow("num_friend"))}"
            tvDescrip.text = cursor_user.getString(cursor_user.getColumnIndexOrThrow("intro"))
            //적용 완료
        }
        cursor_user.close()

        val cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT post_id, picture FROM post WHERE user_id = '${user_id}';", null)
        //사각형 피드를 위한 데이터를 가져옴

        while (cursor_feed.moveToNext()){
            val post_id = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("post_id"))
            val picture = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("picture"))
            //사각형 피드용 데이터 전송
            val item = Feed_Square(post_id, picture)
            itemlist.add(item)
            //itemlist에 추가완료
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
            //프로필 편집 화면으로 전환
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        btnUpload.setOnClickListener {
            //업로드 화면으로 전환
            val intent = Intent(this, UploadPost::class.java)
            startActivity(intent)
        }


    }


   //하단 네비게이션바 기능 추가
    fun setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    true
                }

                R.id.timer -> {
                    val intent = Intent(this, com.example.team24app.Timer::class.java)
                    startActivity(intent)
                    true
                }

//                R.id.profile -> {
////                    val intent = Intent(this, Profile::class.java)
////                    startActivity(intent)
//                    true
//                }

                else -> false

            }

        }
    }
}