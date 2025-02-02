package com.example.team24app

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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

class EditProfile : AppCompatActivity() {
    lateinit var btnBack : ImageButton
    lateinit var ivProfile : ImageView
    lateinit var edtName : EditText
    lateinit var tvName : TextView
    lateinit var btnProfile : Button
    lateinit var btnID : Button
    lateinit var edtDescrip : EditText
    lateinit var btnSave : Button
    private var imageUri : Uri? = null
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var change_id : String
    var isCliecked = false
    var isChecked = false

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
                ivProfile.setImageURI(imageUri)
                //가져온 사진 보여주기
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
        val user_id = UserId.userId
        tvName.text = user_id
        edtName.setText(user_id)

        val cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT profile, intro FROM user WHERE user_id = '"+user_id+"';", null)
        //현재 유저의 프로필을 가져옴

        if (cursor.moveToNext()){
            val pro_string = cursor.getString(cursor.getColumnIndexOrThrow("profile"))
            if(pro_string != "tmp"){
                val uri = Uri.parse(pro_string)
                profile=uri.toString()
                ivProfile.setImageURI(uri)
            }else{
                ivProfile.setImageResource(R.drawable.img)
            }

            edtDescrip.setText(cursor.getString(cursor.getColumnIndexOrThrow("intro")))
        }
        cursor.close()

        btnBack.setOnClickListener {
            //뒤로가기 버튼
            sqlitedb.close()
            dbManager.close()
            onBackPressedDispatcher.onBackPressed()
        }

        btnProfile.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, readImagePermission)== PackageManager.PERMISSION_GRANTED){
                //권한 확인 성공시 갤러리 오픈
                openGallery()
            }else{
                requestPermissionLauncher.launch(readImagePermission)
                //권한 실패시 재시도
            }
        }

        btnID.setOnClickListener {
            //id변경 버튼
            if(isCliecked == false){
                change()
                isCliecked = true
            }else if(isCliecked == true && isChecked == false){
                change_id = edtName.text.toString()
                if(change_id == user_id){
                    Toast.makeText(this, "아이디가 동일합니다.", Toast.LENGTH_SHORT).show()
                }else{
                    check()
                }
            }
        }

        btnSave.setOnClickListener {
            //변경 저장 버튼
            if(imageUri != null){
                profile = imageUri.toString()
                sqlitedb.execSQL("UPDATE user SET profile = '"+profile+"' WHERE user_id = '"+user_id+"';")
            }

            val intro = edtDescrip.text.toString()
            if(!intro.isNullOrBlank()){
                sqlitedb.execSQL("UPDATE user SET intro = '"+intro+"' WHERE user_id = '"+user_id+"';")
            }

            if(isChecked){
                sqlitedb.execSQL("UPDATE post SET user_id = '"+change_id+"' WHERE user_id = '"+user_id+"';")
                sqlitedb.execSQL("UPDATE friend SET from_id = '"+change_id+"' WHERE from_id = '"+user_id+"';")
                sqlitedb.execSQL("UPDATE friend SET to_id = '"+change_id+"' WHERE to_id = '"+user_id+"';")
                sqlitedb.execSQL("UPDATE user SET user_id = '"+change_id+"' WHERE user_id = '"+user_id+"';")
                UserId.userId = change_id
            }
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
            sqlitedb.close()
            dbManager.close()
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }

    fun change(){
        edtName.visibility = View.VISIBLE
        tvName.visibility = View.GONE
        btnID.text = getString(R.string.check)
    }

    fun check(){
        val checkUsername = dbManager!!.checkUser(change_id)
        if (!checkUsername) {
            isChecked = true
            btnID.text = getString(R.string.end)
            btnID.isEnabled=false
            Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(gallery)
        //갤러리를 열기 위해 intent 생성, 갤러리 액티비티 실행
    }
}