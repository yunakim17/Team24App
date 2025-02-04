package com.example.team24app

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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

// 프로필을 변경하는 화면
class EditProfile : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var ivProfile : ImageView
    lateinit var edtName : EditText
    lateinit var tvName : TextView
    lateinit var btnProfile : Button
    lateinit var btnID : Button
    lateinit var edtDescrip : EditText
    lateinit var btnSave : Button
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    private var imageUri : Uri? = null
    lateinit var change_id : String
    var isClicked = false
    var isChecked = false
    val user_id = UserId.userId
    var defaultIntro : String? = null

    // 갤러리 접근 권한 재요청 변수
    private val requestPermissionLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> if (isGranted) openGallery() }

    // 갤러리에서 가져온 사진 이미지뷰에 적용 변수
    private val pickImageLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> if(result.resultCode == RESULT_OK){
            val data:Intent? = result.data

            data?.data?.let{
                imageUri = it
                ivProfile.setImageURI(imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        btnBack = findViewById(R.id.btnBack)
        ivProfile = findViewById(R.id.ivProfileImage)
        edtName = findViewById(R.id.edtUserName)
        tvName = findViewById(R.id.tvUserName)
        btnProfile = findViewById(R.id.btnEdtProfilePic)
        btnID = findViewById(R.id.btnEdtUserId)
        edtDescrip = findViewById(R.id.edtDescription)
        btnSave = findViewById(R.id.btnSaveProfile)
        dbManager = DBManager(this, "appDB", null, 1)
        sqlitedb = dbManager.writableDatabase

        val readImagePermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES
        else android.Manifest.permission.READ_EXTERNAL_STORAGE

        var profile = "tmp"
        tvName.text = user_id
        edtName.setText(user_id)

        val cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT profile, intro FROM user WHERE user_id = ?", arrayOf(user_id))
        //현재 유저의 프로필을 가져옴

        if (cursor.moveToNext()){
            val pro_string = cursor.getString(cursor.getColumnIndexOrThrow("profile"))
            if(pro_string != "tmp"){
                val uri = Uri.parse(pro_string)
                profile=uri.toString()
                ivProfile.setImageURI(uri)
            }else{
                ivProfile.setImageResource(R.drawable.default_profile)
            }

            edtDescrip.setText(cursor.getString(cursor.getColumnIndexOrThrow("intro")))
        }
        cursor.close()

        //뒤로가기 버튼
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // 프로필 사진 변경 버튼
        btnProfile.setOnClickListener {
            // 권한 확인 성공시 갤러리 오픈 / 실패시 재시도
            if(ContextCompat.checkSelfPermission(this, readImagePermission)== PackageManager.PERMISSION_GRANTED) openGallery()
            else requestPermissionLauncher.launch(readImagePermission)
        }

        // ID 변경 버튼
        btnID.setOnClickListener {
            // 변경 버튼을 누르면 중복 확인 버튼으로 변경
            if(isClicked == false){
                change()
                isClicked = true
            }else if(isClicked == true && isChecked == false){
                change_id = edtName.text.toString()
                if(change_id == user_id){
                    Toast.makeText(this, "아이디가 동일합니다.", Toast.LENGTH_SHORT).show()
                }else{
                    check()
                }
            }
        }

        // 변경 저장 버튼
        btnSave.setOnClickListener {
            if(imageUri != null){
                profile = saveImage(imageUri!!)
                sqlitedb.execSQL("UPDATE user SET profile = ? WHERE user_id = ?", arrayOf(profile, user_id))
            }

            val intro = edtDescrip.text.toString()
            if(!intro.isNullOrBlank()){
                sqlitedb.execSQL("UPDATE user SET intro = ? WHERE user_id = ?", arrayOf(intro, user_id))
            }

            if(isChecked){
                sqlitedb.execSQL("UPDATE post SET user_id = ? WHERE user_id = ?", arrayOf(change_id, user_id))
                sqlitedb.execSQL("UPDATE follow SET from_id = ? WHERE from_id = ?", arrayOf(change_id, user_id))
                sqlitedb.execSQL("UPDATE follow SET to_id = ? WHERE to_id = ?", arrayOf(change_id, user_id))
                sqlitedb.execSQL("UPDATE user SET user_id = ? WHERE user_id = ?", arrayOf(change_id, user_id))
                UserId.userId = change_id
            }
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            sqlitedb.close()
            dbManager.close()

            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    // 아이디 뷰를 edittext로 전환
    fun change(){
        edtName.visibility = View.VISIBLE
        tvName.visibility = View.GONE
        btnID.text = getString(R.string.check)
    }

    // 아이디 중복 체크/길이 체크
    fun check(){
        change_id = edtName.text.toString()

        // 현재 아이디와 동일한지 검사 / 아이디 길이 검사 / 중복 검사를 마치고 버튼 비활성화
        if(change_id == user_id) Toast.makeText(this, "아이디가 동일합니다.", Toast.LENGTH_SHORT).show()
        else if(change_id.length < 5 || change_id.length > 10) Toast.makeText(this, "아이디를 5~10글자 내로 설정해주세요.", Toast.LENGTH_SHORT).show()
        else if(dbManager.checkUser(change_id)) Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
        else {
            val drawable = GradientDrawable()

            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(ContextCompat.getColor(this, R.color.disable_btn_color))
            drawable.cornerRadius = 25F

            btnID.background = drawable
            isChecked = true
            btnID.text = getString(R.string.end)

            Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //갤러리 오픈 함수
    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
    }

    // 이미지를 기기 내부 저장소에 저장하며 경로를 저장하는 함수
    fun saveImage(uri : Uri):String{
        val inputStream : InputStream? = contentResolver.openInputStream(uri)
        val file = File(filesDir, "${user_id}_profile_image.jpg")

        //프로필 사진 내부 저장소에 저장
        file.outputStream().use{ fileOutput ->
            inputStream?.copyTo(fileOutput)
        }
        inputStream?.close()

        //내부 저장소의 절대경로 복사
        return file.absolutePath
    }
}