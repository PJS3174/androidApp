package com.example.fbfirst

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var loginText: EditText
    private lateinit var passText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var warningText: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginText = findViewById(R.id.loginEmail)
        passText = findViewById(R.id.loginPassword)
        loginText.setText(null)
        passText.setText(null)

        warningText = findViewById(R.id.warning) // 틀릴 경우 경고문
        loginButton = findViewById(R.id.signIn) // 로그인 버튼
        signUpButton = findViewById(R.id.signUp) // 회원가입 버튼

        loginButton.setOnClickListener { //로그인 버튼
            if(loginText.text.toString().isEmpty()){
                warningText.text="이메일을 입력하세요."
            }
            if(passText.text.toString().isEmpty()){
                warningText.text="비밀번호를 입력하세요."
            }

            if(loginText.text.toString().isNotEmpty() && passText.text.toString().isNotEmpty())
            Firebase.auth.signInWithEmailAndPassword(loginText.text.toString(), passText.text.toString())
                .addOnCompleteListener(this) { // it: Task<AuthResult!>
                    if (it.isSuccessful) {
                        val intent = Intent(this, SalesList::class.java)
                        intent.putExtra("ID", loginText.text.toString())
                        startActivity(intent)
                    } else {
                        warningText.text="이메일 또는 비밀번호를 확인하세요"
                    }
                }
        }

        signUpButton.setOnClickListener{// 회원가입 버튼
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

    }

}