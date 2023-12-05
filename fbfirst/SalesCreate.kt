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

class SalesCreate : AppCompatActivity() {
    private val database = Firebase.database
    private val user = Firebase.auth.currentUser
    private var uid = user?.uid
    private var path = "items/$uid"
    private val itemsRef = database.getReference(path)
    private lateinit var title: TextView
    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var check: TextView
    private lateinit var detail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_create)
        queryItem("list")

        val button = findViewById<Button>(R.id.backList)
        button.setOnClickListener {
            val intent = Intent(this, SalesList::class.java)
            startActivity(intent)
        }

        val completeButton = findViewById<Button>(R.id.complete)
        completeButton.setOnClickListener {

            itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val numberOfLists = dataSnapshot.childrenCount.toInt()
                    val nextListNumber = numberOfLists + 1

                    val itemRef = itemsRef.child("list$nextListNumber")

                    val itemMap = hashMapOf( // 여러 자식(키,값)을 한번에 쓰기
                        "title" to title.text.toString(),
                        "price" to price.text.toString(),
                        "check" to check.text.toString(),
                        "detail" to detail.text.toString()
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
        title = findViewById(R.id.salesTitle)
        name = findViewById(R.id.saler)
        price = findViewById(R.id.salesPrice)
        check = findViewById(R.id.salesCheck)
        detail = findViewById(R.id.salesDetail)

        itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as? Map<*, *>
                if(map != null) {
                    name.text = map["name"].toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }) // 판매자 이름을 표시

        itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as? Map<*, *>
                if(map != null) {
                    title.text = ""
                    check.text = "판매중"
                    price.text = ""
                    detail.text = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }) // 그외의 내용을 표시
    }
}