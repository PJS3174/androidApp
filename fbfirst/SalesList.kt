package com.example.fbfirst

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class SalesList : AppCompatActivity() {
    private val database = Firebase.database
    private val user = Firebase.auth.currentUser
    private var uid = user?.uid
    private val itemsRef = database.getReference("items")
    private var auth = Firebase.auth
    private lateinit var listView: ListView
    private lateinit var adapter: Adapter
    private var receivename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_list)

        listView = findViewById<View>(R.id.Chatting) as ListView
        adapter = Adapter(this)
        listView.adapter = adapter
        queryName(uid.toString())

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

        val button2 = findViewById<Button>(R.id.salesCreate)
        button2.setOnClickListener {
            val intent = Intent(this, SalesCreate::class.java)
            startActivity(intent)
        }// 글 작성

    }

    private fun queryItem(itemID: String) { // 데이터 가져오기
        val baseRef = itemsRef.child(itemID)

        for (i in 1..100) {
            val listRef = baseRef.child("list$i")
            listRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map = dataSnapshot.value as? Map<*, *>
                    if (map != null) {
                        adapter.addItem(map["title"].toString(), map["check"].toString(), map["price"].toString(), itemID, "list$i")
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun queryName(itemID: String) {
        val baseRef = itemsRef.child(itemID)

        baseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as? Map<*, *>
                if(map != null) {
                    receivename = map["name"].toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.every->
                adapter.orignItems()
            R.id.saleing->
                adapter.filterItems("판매중")
            R.id.complete->
                adapter.filterItems("판매완료")
            R.id.chatting->
                startActivity(Intent(this, Chatting::class.java).putExtra("key1", receivename))

        }
        return true
    }

}