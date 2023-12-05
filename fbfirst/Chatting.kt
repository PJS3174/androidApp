package com.example.fbfirst

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Chatting : AppCompatActivity() {
    private val database = Firebase.database
    private val user = Firebase.auth.currentUser
    private var uid = user?.uid
    private val itemsRef = database.getReference("items")
    private var auth = Firebase.auth
    private lateinit var listView: ListView
    private lateinit var adapter: ChattingAdapter
    private var receivename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatting)
        val receivedValue = intent.getStringExtra("key1") // name
        receivename = receivedValue.toString()

        listView = findViewById<View>(R.id.Chatting) as ListView
        adapter = ChattingAdapter(this)
        listView.adapter = adapter

        itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (itemSnapshot in snapshot.children){
                    uid=itemSnapshot.key
                    var currentUser = auth.currentUser
                    currentUser?.let {
                        queryItem(uid.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val button = findViewById<Button>(R.id.logOut)
        button.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } // 로그아웃

        val button2 = findViewById<Button>(R.id.returnChattingList)
        button2.setOnClickListener {
            val intent = Intent(this, SalesList::class.java)
            startActivity(intent)
        }

    }

    private fun queryItem(itemID: String) { // 데이터 가져오기
        val baseRef = itemsRef.child(itemID)
        for (i in 1..100) {
            val listRef = baseRef.child("message$i")
            listRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = dataSnapshot.value as? Map<*, *>
                    if (map != null) {
                        if(map["senduser"].toString() == receivename) {
                            adapter.addCItem(
                                map["username"].toString(),
                                map["chatdetail"].toString(),
                                map["senduser"].toString(),
                                itemID,
                                "list"
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

}