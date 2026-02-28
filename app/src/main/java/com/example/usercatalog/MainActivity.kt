package com.example.usercatalog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.usercatalog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var userViewModel: UserViewModel

    private lateinit var adapter: ArrayAdapter <User> //стандартный ArrayAdapter
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTB)

        //Viewmodel инициализация
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        //инициализация стандартного ArrayAdapter
        //используем простой layout для отображения
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, //стандартный layout Android
            mutableListOf()) //пустой список изначально
        binding.usersLV.adapter = adapter


        //подписка на изменения списка пользователей через observe
        userViewModel.users.observe(this,{ userList ->
            //когда данные в LievData изменяются, обновляем Adapter
            adapter.clear() //очищаем текущие данные
            adapter.addAll(userList)//добавляем все элементы из нового списка
            adapter.notifyDataSetChanged() //обновляем отображение

        })

        binding.saveBTN.setOnClickListener {
            saveUser()
        }


        binding.usersLV.setOnItemClickListener { parent, view, position, id ->
            MyDialog.createDialog(this, adapter)
        }
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
        userViewModel.addUser(user)
        //очистка полей ввода
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