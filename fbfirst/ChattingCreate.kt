package com.example.fbfirst

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ChattingCreate : AppCompatActivity() {
    private val database = Firebase.database
    private val user = Firebase.auth.currentUser
    private var uid = user?.uid
    private var path = "items/$uid"
    private var itemsRef = database.getReference(path)
    private lateinit var ChatDetail: TextView
    private lateinit var UserName: TextView
    private lateinit var SendUser: TextView
    private var Username = ""
    private var Senduser = ""
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatting_create)
        val username = intent.getStringExtra("name")
        Username = username.toString()
        val senduser = intent.getStringExtra("sender")
        Senduser = senduser.toString()
        val userid = intent.getStringExtra("uid")
        userId = userid.toString()

        queryItem("message")

        val retrunButton = findViewById<Button>(R.id.retrunChatList)
        retrunButton.setOnClickListener {
            val intent = Intent(this, SalesList::class.java)
            startActivity(intent)
        }

        val sendButton = findViewById<Button>(R.id.send)
        sendButton.setOnClickListener {
            itemsRef = database.getReference("items/$userId")
            itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val numberOfLists = dataSnapshot.childrenCount.toInt()
                    val nextListNumber = numberOfLists + 1

                    val itemRef = itemsRef.child("message$nextListNumber")
                    val itemMap = hashMapOf( // 여러 자식(키,값)을 한번에 쓰기
                        "chatdetail" to ChatDetail.text.toString(),
                        "username" to SendUser.text.toString(),
                        "senduser" to UserName.text.toString()
                    )

                    itemRef.setValue(itemMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            itemRef.setValue(itemMap)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 오류 처리
                }
            })

            val intent = Intent(this, SalesList::class.java)
            startActivity(intent)
        }
    }

    private fun queryItem(itemID: String) { // 데이터 가져오기
        ChatDetail = findViewById(R.id.editChat)
        UserName = findViewById(R.id.username)
        SendUser = findViewById(R.id.sendUser)

        itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as? Map<*, *>
                if(map != null) {
                    ChatDetail.text = ""
                    UserName.text = Username
                    SendUser.text = map["name"].toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }) // 그외의 내용을 표시
    }
}