package com.example.usercatalog

import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyDialog {
    companion object {
        fun createDialog(context: Context, adapter: ArrayAdapter<User?>) =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Удаление")
                    .setMessage("Удалить пользователя?")
                    .setCancelable(true)
                    .setNegativeButton("Нет") { dialog, which ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Да") { dialog, which ->
                        val user = adapter.getItem(position)
                        adapter.remove(user)
                        Toast.makeText(
                            context,
                            "Пользователь удален",
                            Toast.LENGTH_LONG
                        ).show()
                    }.create()
                builder.show()
            }
    }
}