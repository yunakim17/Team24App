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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class UploadPost : AppCompatActivity() {
    lateinit var btnBack : ImageButton
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
        btnBack = findViewById(R.id.btnBack)
        ivPhoto = findViewById(R.id.SelectedPhoto)
        btnPhoto = findViewById(R.id.btnPhoto)
        tvHour = findViewById(R.id.tvHours)
        tvMin = findViewById(R.id.tvMinutes)
        tvSec = findViewById(R.id.tvSeconds)
        edtComment = findViewById(R.id.edtComment)
        btnUpload = findViewById(R.id.btnUpload)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        val readImagePermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE
        //권한 습득

        val hour = Time.hour
        val minute = Time.minute
        val second = Time.second
        tvHour.text = "$hour"
        tvMin.text = "$minute"
        tvSec.text = "$second"
        //타이머에서 설정한 시간/날짜 설정

        btnBack.setOnClickListener {
            //뒤로가기 버튼
            onBackPressedDispatcher.onBackPressed()
        }

        btnPhoto.setOnClickListener {
            //사진 업로드
            if(ContextCompat.checkSelfPermission(this, readImagePermission)==PackageManager.PERMISSION_GRANTED){
                //권한 확인 성공시 갤러리 오픈
                openGallery()
            }else{
                requestPermissionLauncher.launch(readImagePermission)
                //권한 실패시 재시도
            }
        }

        btnUpload.setOnClickListener {
            //업로드 버튼
            var num = 0
            val user_id = UserId.userId
            val picture = imageUri.toString()
            val comment = edtComment.text.toString()
            val date = Time.date
            if(picture=="null"){
                //사진이 비었는지 확인
                Toast.makeText(this, "사진이 설정되지 않았습니다. 사진을 설정해주세요.",Toast.LENGTH_SHORT).show()
            }else{
                val cursor_num : Cursor
                cursor_num = sqlitedb.rawQuery("SELECT count(*) as cnt FROM post;", null)
                //post_id 설정

                if (cursor_num.moveToNext()){
                    num = cursor_num.getInt(cursor_num.getColumnIndexOrThrow("cnt")) + 1
                }
                cursor_num.close()

                sqlitedb.execSQL("INSERT INTO post VALUES ("+num+", '"+user_id+"', '"+picture+"', 0, '"+comment+"', '"+date+"', "+hour+", "+minute+", "+second+");")
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        sqlitedb.close()
        dbManager.close()
    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
        //갤러리를 열기 위해 intent 생성, 갤러리 액티비티 실행
    }

}