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
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

// 포스트 업로드 화면
class UploadPost : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var ivPicture : ImageView
    lateinit var btnPicture : ImageButton
    lateinit var tvHour : TextView
    lateinit var tvMin : TextView
    lateinit var tvSec : TextView
    lateinit var edtComment : EditText
    lateinit var btnUpload : Button
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    private var imageUri : Uri? = null
    val user_id = UserId.userId

    // 갤러리 접근 권한 재요청 변수
    private val requestPermissionLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> if (isGranted) openGallery() }

    // 갤러리에서 가져온 사진 이미지뷰에 적용 변수
    private val pickImageLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> if(result.resultCode == RESULT_OK){
            val data:Intent? = result.data

            data?.data?.let{
                imageUri = it
                ivPicture.setImageURI(imageUri)
            }
        }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)
        btnBack = findViewById(R.id.btnBack)
        ivPicture = findViewById(R.id.SelectedPhoto)
        btnPicture = findViewById(R.id.btnPhoto)
        tvHour = findViewById(R.id.tvHours)
        tvMin = findViewById(R.id.tvMinutes)
        tvSec = findViewById(R.id.tvSeconds)
        edtComment = findViewById(R.id.edtComment)
        btnUpload = findViewById(R.id.btnUpload)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        val readImagePermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

        // 타이머에서 설정한 시간/날짜 설정
        tvHour.text = "${Time.hour}"
        tvMin.text = "${Time.minute}"
        tvSec.text = "${Time.second}"

        // 뒤로가기 버튼
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 사진 업로드
        btnPicture.setOnClickListener {
            // 권한 확인 성공시 갤러리 오픈 / 실패시 재시도
            if(ContextCompat.checkSelfPermission(this, readImagePermission)== PackageManager.PERMISSION_GRANTED) openGallery()
            else requestPermissionLauncher.launch(readImagePermission)
        }

        // 업로드 버튼
        btnUpload.setOnClickListener {
            // 사진이 비었는지 확인
            if(imageUri == null){
                //사진이 비었는지 확인
                Toast.makeText(this, "사진이 설정되지 않았습니다.\n사진을 설정해주세요.",Toast.LENGTH_SHORT).show()
            }else {
                var num = 0
                val cursor_num: Cursor
                cursor_num = sqlitedb.rawQuery("SELECT count(*) as cnt FROM post;", null)
                //post_id 설정

                if (cursor_num.moveToNext()) {
                    num = cursor_num.getInt(cursor_num.getColumnIndexOrThrow("cnt")) + 1
                }
                cursor_num.close()

                val picture = saveImage(imageUri!!, num)
                val comment = edtComment.text.toString()
                val now = Date()
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", java.util.Locale.KOREA)
                val date = dateFormat.format(now)

                sqlitedb.execSQL("INSERT INTO post VALUES (?, ?, ?, 0, ?, ?, ?, ?, ?)", arrayOf(num, user_id, picture, comment, date, Time.hour, Time.minute, Time.second))
                sqlitedb.close()
                dbManager.close()

                val intent = Intent(this, Profile::class.java)
                startActivity(intent)
            }
        }
    }

    //갤러리 오픈 함수
    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

    // 이미지를 기기 내부 저장소에 저장하며 경로를 저장하는 함수
    fun saveImage(uri : Uri, num : Int):String{
        val inputStream : InputStream? = contentResolver.openInputStream(uri)
        val file = File(filesDir, "${user_id}_post_image_${num}.jpg")

        //프로필 사진 내부 저장소에 저장
        file.outputStream().use{ fileOutput ->
            inputStream?.copyTo(fileOutput)
        }
        inputStream?.close()

        //내부 저장소의 절대경로 복사
        return file.absolutePath
    }
}