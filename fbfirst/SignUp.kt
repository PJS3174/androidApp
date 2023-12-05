package com.example.fbfirst

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class SignUp : AppCompatActivity() {
    private lateinit var createButton: Button
    private lateinit var cancleButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var pwEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var birthEditText: EditText
    private lateinit var warningText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        val database = Firebase.database
        val itemsRef = database.getReference("items")

        createButton = findViewById(R.id.create)
        cancleButton = findViewById(R.id.cancle)
        emailEditText = findViewById(R.id.emailEdit)
        pwEditText = findViewById(R.id.pwEdit)
        nameEditText = findViewById(R.id.nameEdit)
        birthEditText = findViewById(R.id.birthEdit)
        warningText = findViewById(R.id.warning)

        createButton.setOnClickListener{// 계정 추가 후 바로 로그인
            if(emailEditText.text.toString() != "" && pwEditText.text.toString() != "" && nameEditText.text.toString() != "" && birthEditText.text.toString() != "") {

                Firebase.auth.createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    pwEditText.text.toString()
                )
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            val user = Firebase.auth.currentUser // 계정 추가
                            val uid = user?.uid
                            val itemMap = hashMapOf( // 여러 자식(키,값)을 한번에 쓰기
                                "email" to emailEditText.text.toString(),
                                "name" to nameEditText.text.toString(),
                                "birthday" to birthEditText.text.toString(),
                                "password" to pwEditText.text.toString()
                            )
                            val itemRef = itemsRef.child(uid.toString())
                            itemRef.setValue(itemMap)

                            Firebase.auth.signInWithEmailAndPassword(
                                emailEditText.text.toString(),
                                pwEditText.text.toString()
                            )
                                .addOnCompleteListener(this) { // it: Task<AuthResult!>
                                    if (it.isSuccessful) {
                                        val intent = Intent(this, SalesList::class.java)
                                        intent.putExtra("ID", emailEditText.text.toString())
                                        startActivity(intent)
                                    }
                                }
                        }
                    }
                    .addOnFailureListener(this){
                        if(it is FirebaseAuthUserCollisionException){
                            warningText.text = "이미 가입된 이메일 입니다."
                        }
                    }
            }
            else{
                warningText.text = "정보를 모두 입력하세요"
            }
        }

        cancleButton.setOnClickListener{ // 회원가입 취소
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}