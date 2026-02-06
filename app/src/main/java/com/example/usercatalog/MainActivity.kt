package com.example.usercatalog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.usercatalog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var users: MutableList<User> = mutableListOf()

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTB)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        binding.usersLV.adapter = adapter

        binding.saveBTN.setOnClickListener {
            saveUser()
            adapter.notifyDataSetChanged()
        }


        binding.usersLV.onItemClickListener =
            MyDialog.createDialog(this, adapter)
    }

    private fun saveUser() {
        val name = binding.nameET.text.toString().trim()
        val ageText = binding.ageET.text.toString().trim()

        if (name.isEmpty()) {
            showToast("Введите имя")
            return
        }
        if (ageText.isEmpty()) {
            showToast("Введите возраст")
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null || age <= 0) {
            showToast("Введите корректный возраст")
        }

        val user = User(name, age)
        users.add(user)

        clearInputFields()

        showToast("Пользователь $name добавлен")
    }

    private fun clearInputFields() {
        binding.nameET.text.clear()
        binding.ageET.text.clear()
        binding.nameET.requestFocus()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exit, menu)
        return true
    }

    //обработка выбора пункта меню

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}