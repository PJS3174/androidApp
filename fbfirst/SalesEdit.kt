package com.example.fbfirst

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SalesEdit : AppCompatActivity(){
    private val database = Firebase.database
    private var path = "items"
    private var itemsRef = database.getReference(path)
    private lateinit var title: TextView
    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var check: TextView
    private lateinit var detail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_edit)
        val receivedValue1 = intent.getStringExtra("key1")
        val receivedValue2 = intent.getStringExtra("key2")

        itemsRef = database.getReference("$path/$receivedValue1")
        queryItem("$receivedValue2")

        val button = findViewById<Button>(R.id.backList)
        button.setOnClickListener {
            val intent = Intent(this, SalesList::class.java)
            startActivity(intent)
        }

        val completeButton = findViewById<Button>(R.id.complete)
        completeButton.setOnClickListener {
            val itemMap = hashMapOf( // 여러 자식(키,값)을 한번에 쓰기
                "title" to title.text.toString(),
                "price" to price.text.toString(),
                "check" to check.text.toString(),
                "detail" to detail.text.toString()
            )

            val itemRef = itemsRef.child("$receivedValue2")
            itemRef.setValue(itemMap)

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
                    name.text = (map["name"].toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }) // 판매자 이름을 표시

        itemsRef.child(itemID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.value as? Map<*, *>
                if(map != null) {
                    title.text = (map["title"].toString())
                    check.text = (map["check"].toString())
                    price.text = (map["price"].toString())
                    detail.text = (map["detail"].toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }) // 그외의 내용을 표시
    }
}