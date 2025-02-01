package com.example.team24app

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class UploadPost : AppCompatActivity() {
    lateinit var ivPhoto : ImageView
    lateinit var btnPhoto : ImageButton
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var edtComment : EditText
    lateinit var btnUpload : Button
    private var imageUri : Uri? = null
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    private val requestPermissionLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted -> if (isGranted){
            //권한 성공시 갤러리 오픈
            openGallery()
        }
    }

    private val pickImageLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result -> if(result.resultCode == RESULT_OK){
            val data:Intent? = result.data
            data?.data?.let{
                imageUri = it
                ivPhoto.setImageURI(imageUri)
                //가져온 사진 보여주기
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)
        ivPhoto = findViewById<ImageView>(R.id.SelectedPhoto)
        btnPhoto = findViewById<ImageButton>(R.id.btnPhoto)
        tvHour = findViewById<TextView>(R.id.tvHours)
        tvMin = findViewById<TextView>(R.id.tvMinutes)
        tvSec = findViewById<TextView>(R.id.tvSeconds)
        edtComment = findViewById<EditText>(R.id.edtComment)
        btnUpload = findViewById<Button>(R.id.btnUpload)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        val readImagePermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

        btnPhoto.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, readImagePermission)==PackageManager.PERMISSION_GRANTED){
                //권한 확인 성공시 갤러리 오픈
                openGallery()
            }else{
                requestPermissionLauncher.launch(readImagePermission)
                //권한 실패시 재시도
            }
        }

        val intent = intent
        val user_id = "tmp"
        //시간 업로드는 타이머 버튼시 intent로 전달 예정



        btnUpload.setOnClickListener {
            val cursor_num : Cursor
            cursor_num = sqlitedb.rawQuery("SELECT count(*) FROM feed;", null)
            val num = cursor_num.getInt(0) + 1

            sqlitedb.execSQL("INSERT INTO feed VALUES ();")
        }
    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
        //갤러리를 열기 위해 intent 생성, 갤러리 액티비티 실행
    }


}