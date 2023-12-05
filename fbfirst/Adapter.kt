package com.example.fbfirst

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Items {
    var title: String? = null
    var check: String? = null
    var price: String? = null
    var uid: String? = null
    var listnum: String? = null
}

class Adapter(private val context: Context) : BaseAdapter(){
    val user = Firebase.auth.currentUser
    var uid = user?.uid

    private var ListView = ArrayList<Items>()
    private var OriginalView  = ArrayList<Items>()

    override fun getCount(): Int {
        return ListView.size;
    }

    override fun getItem(p0: Int): Any {
        return ListView [p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItem = convertView
        var context = parent.context

        if (listItem == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            listItem = inflater.inflate(R.layout.list, parent, false)
        }

        val title = listItem!!.findViewById(R.id.salesTitle) as TextView
        val check = listItem!!.findViewById(R.id.saleCheck) as TextView
        val price = listItem!!.findViewById(R.id.salePrice) as TextView

        val listViewItem = ListView[position]

        title.setText(listViewItem.title)
        check.setText(listViewItem.check)
        price.setText(listViewItem.price)

        val showItem = listItem.findViewById<Button>(R.id.show)
        showItem.setOnClickListener {
            if(listViewItem.uid==uid) {
                val intent = Intent(context, SalesEdit::class.java)
                intent.putExtra("key1", listViewItem.uid)
                intent.putExtra("key2", listViewItem.listnum)
                context.startActivity(intent)
            }else {
                val intent = Intent(context, Sales::class.java)
                intent.putExtra("key1", listViewItem.uid)
                intent.putExtra("key2", listViewItem.listnum)

                context.startActivity(intent)
            }
        }

        return listItem
    }

    fun addItem(title: String, check: String, price: String, uid: String, listnum: String) {
        val item = Items()

        item.title = title
        item.check = check
        item.price = price
        item.uid = uid
        item.listnum = listnum

        ListView.add(item)
        OriginalView.add(item)
    }

    fun filterItems(condition: String) {
        ListView.clear()

        for (item in OriginalView) {
            if (item.check == condition) {
                ListView.add(item)
            }
        }

        notifyDataSetChanged() // 필터링
    }

    fun orignItems() {
        ListView.clear()
        ListView.addAll(OriginalView)

        notifyDataSetChanged() // 전체글보기
    }
}