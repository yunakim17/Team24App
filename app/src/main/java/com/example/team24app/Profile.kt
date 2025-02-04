package com.example.team24app

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

// 프로필 화면
class Profile : AppCompatActivity() {
    lateinit var tvName : TextView
    lateinit var ivProfile : ImageView
    lateinit var tvFollow : TextView
    lateinit var tvDescrip : TextView
    lateinit var btnProfile : Button
    lateinit var btnUpload : Button
    lateinit var rvProfile : RecyclerView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 피드에 아이템이 없을 때 보이는 텍스트뷰/이미지뷰
    lateinit var emptyViewPf: TextView
    lateinit var emptyImagePf: ImageView

    // 하단네비뷰
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvUserName)
        ivProfile = findViewById(R.id.ivProfileImage)
        tvFollow = findViewById(R.id.tvFollowCount)
        tvDescrip = findViewById(R.id.tvDescription)
        btnProfile = findViewById(R.id.btnEdtProfile)
        btnUpload = findViewById(R.id.btnPostUpload)
        rvProfile = findViewById(R.id.rvPosts)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 텍스트,이미지뷰 연결
        emptyViewPf = findViewById(R.id.noItemText)
        emptyImagePf = findViewById(R.id.noItemImage)

        // 하단네비뷰 연결
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setBottomNavigationView()



        val itemList = ArrayList<Post_Square>()

        val user_id = UserId.userId
        tvName.text = user_id

        val cursor_user : Cursor
        cursor_user = sqlitedb.rawQuery("SELECT profile, num_follow, intro FROM user WHERE user_id = ?", arrayOf(user_id))
        //user_id로 프로필 데이터를 가져옴

        if(cursor_user.moveToNext()){
            //val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            //this.contentResolver.takePersistableUriPermission(uri, takeFlags)
            val profile = cursor_user.getString(cursor_user.getColumnIndexOrThrow("profile"))
            if(profile != "tmp"){
                val uri = Uri.fromFile(File(profile))
                ivProfile.setImageURI(uri)
            }else{
                ivProfile.setImageResource(R.drawable.default_profile)
            }

            tvFollow.text = "${cursor_user.getInt(cursor_user.getColumnIndexOrThrow("num_follow"))}"
            tvDescrip.text = cursor_user.getString(cursor_user.getColumnIndexOrThrow("intro"))
            //적용 완료
        }
        cursor_user.close()

        val cursor_feed : Cursor
        cursor_feed = sqlitedb.rawQuery("SELECT post_id, picture FROM post WHERE user_id = ? ORDER BY post_id DESC;", arrayOf(user_id))
        //사각형 피드를 위한 데이터를 가져옴

        while (cursor_feed.moveToNext()){
            val post_id = cursor_feed.getInt(cursor_feed.getColumnIndexOrThrow("post_id"))
            val picture = cursor_feed.getString(cursor_feed.getColumnIndexOrThrow("picture"))
            //사각형 피드용 데이터 전송
            val item = Post_Square(post_id, picture)
            itemList.add(item)
            //itemlist에 추가완료
        }
        cursor_feed.close()
        sqlitedb.close()
        dbManager.close()

        // 리사이클러뷰 어댑터 연결 완료
        val rv_adapter = SquareAdapter(itemList, this)
        rv_adapter.notifyDataSetChanged()
        rvProfile.adapter=rv_adapter
        rvProfile.layoutManager= GridLayoutManager(this, 3)

        // 메서드 호출
        checkIfRecyclerViewIsEmpty(rv_adapter)



        // 프로필 편집 버튼
        btnProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        //업로드 버튼
        btnUpload.setOnClickListener {
            val intent = Intent(this, UploadPost::class.java)
            startActivity(intent)
        }
    }

    //리사이클러뷰 아이템x 메서드
    private fun checkIfRecyclerViewIsEmpty(adapter: RecyclerView.Adapter<*>){
        if(adapter.itemCount == 0){
            emptyViewPf.visibility = View.VISIBLE
            emptyImagePf.visibility = View.VISIBLE
        }else{
            emptyViewPf.visibility = View.GONE
            emptyImagePf.visibility = View.GONE
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

                else -> false
            }
        }
    }
}