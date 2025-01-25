package com.example.team24app

import android.content.Intent
import android.content.pm.PackageManager
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
    lateinit var btnPhoto : Button
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var edtComment : EditText
    lateinit var btnUpload : Button
    private var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)
        ivPhoto = findViewById<ImageView>(R.id.SelectedPhoto)
        val btnPhoto = findViewById<ImageButton>(R.id.btnPhoto)
        tvHour = findViewById<TextView>(R.id.tvHours)
        tvMin = findViewById<TextView>(R.id.tvMinutes)
        tvSec = findViewById<TextView>(R.id.tvSeconds)
        edtComment = findViewById<EditText>(R.id.edtComment)
        btnUpload = findViewById<Button>(R.id.btnUpload)

        //시간 업로드는 타이머 버튼시 intent로 전달 예정

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

        btnUpload.setOnClickListener {
            //피드 테이블에 업로드
        }
    }

    private val requestPermissionLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted -> if (isGranted){
            //권한 성공시 갤러리 오픈
            openGallery()
        }
    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
        //갤러리를 열기 위해 intent 생성, 갤러리 액티비티 실행
    }

    private val pickImageLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode == RESULT_OK){
                val data:Intent? = result.data
                data?.data?.let{
                    imageUri = it
                    ivPhoto.setImageURI(imageUri)
                }
            }
    }
}