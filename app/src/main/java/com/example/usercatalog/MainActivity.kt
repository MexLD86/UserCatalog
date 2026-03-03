package com.example.usercatalog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.usercatalog.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        //обработчик клика по элементу списка
        binding.usersLV.setOnItemClickListener { _, _, position, _ ->
            showMaterialDeleteDialog(position)
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
        if (age == null || age <= 0 || age > 120) {
            showToast("Введите корректный возраст")
            return
        }

//        val userId = userViewModel.addUser(name, age)
        //очистка полей ввода
        clearInputFields()

        showToast("Пользователь $name добавлен.")
    }

    fun showMaterialDeleteDialog (position: Int) {
        val userToDelete = userViewModel.getUserById(position) ?: return

        MaterialAlertDialogBuilder(this)
            .setTitle("Удаление пользователя")
            .setMessage("Удалить ${userToDelete.name} (${userToDelete.age} лет)?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Удалить") { _, _ ->
                deleteUserById(position)
            }
            .setNegativeButton("Отмена") {dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Подробнее") { _, _, ->
                //дополнительная кнопка для информации
                showUserInfoDialog(userToDelete)
            }
            .show()
    }


    private fun showDeleteConfirmationDialog(position: Int) {
        val userToDelete = userViewModel.getUserById(position)

        if (userToDelete == null) {
            showToast("Ошибка: пользователь не найден")
            return
        }
        val dialog = AlertDialog.Builder(this)
            .setTitle("Удаление пользователя")
            .setMessage("Вы уверены, что хотите удалить пользователя ${userToDelete.name} (${userToDelete.age} лет)?")
            .setPositiveButton("Да") {_, _, ->
                //вызываем удаление при нажатии "Да"
                deleteUserById(position)
            }
            .setNegativeButton("Нет") { dialog, which ->
                dialog.cancel()//просто закрываем диалог
            }
            .setNeutralButton("Подробнее") { _, _, ->
                //дополнительная кнопка для информации
                showUserInfoDialog(userToDelete)

            }
            .setCancelable(false) //нельзя закрыть кнопкой "Назад"
            .create()
        //показываем диалог
        dialog.show()

        //можно изменить цвета кнопок
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor((android.R.color.holo_green_dark)))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor((android.R.color.holo_red_dark)))

    }
    private fun showUserInfoDialog(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Инфорсация о пользователе")
            .setMessage("Имя ${user.name}\n Возраст ${user.age} лет")
            .setPositiveButton("Хорошо", null)
            .show()
    }
    private fun deleteUserById(id: Int) {
        //получаем пользователя до удаления (для нейтральной кнопки)
        val userToDelete = userViewModel.getUserById(id)

        //удаляем через ViewModel
        val isDelete = userViewModel.removeUserById(id)
        if (isDelete && userToDelete != null) {
            showToast("Пользователь ${userToDelete.name} успешно удален")
        } else {
            showToast("Ошибка при удалении пользователя")
        }
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