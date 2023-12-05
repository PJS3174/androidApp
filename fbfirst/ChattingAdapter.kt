package com.example.fbfirst

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class cItems {
    var title: String? = null
    var detail: String? = null
    var senduser: String? = null
    var uid: String? = null
    var listnum: String? = null
}

class ChattingAdapter(private val context: Context) : BaseAdapter(){
    val user = Firebase.auth.currentUser

    private var ListView = ArrayList<cItems>()

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
            listItem = inflater.inflate(R.layout.chat, parent, false)
        }

        val title = listItem!!.findViewById(R.id.chatTitle) as TextView
        val detail = listItem!!.findViewById(R.id.chatDetail) as TextView
        val listViewItem = ListView[position]

        title.setText(listViewItem.title)
        detail.setText(listViewItem.detail)

        return listItem
    }

    fun addCItem(title: String, detail: String, senduser: String, uid: String, listnum: String) {
        val citem = cItems()

        citem.title = title
        citem.detail = detail
        citem.senduser = senduser
        citem.uid = uid
        citem.listnum = listnum

        ListView.add(citem)
    }

}