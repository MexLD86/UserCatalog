package com.example.usercatalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    //Единственный MutableLiveData, который хранит список пользователей
    private val userList = MutableLiveData<MutableList<User>>(mutableListOf())


    val users: LiveData<MutableList<User>> = userList

    //метод для добавления пользователя
    fun addUser(name: String, age: Int): Int {
        val currentList = userList.value ?: mutableListOf()
        val user = User(name, age)
        currentList.add(user)
        userList.value = currentList  //обновляем LiveData
        return currentList.size - 1
    }

    fun removeUserById(id: Int): Boolean {
        val currentList = userList.value ?: return false

        return if (id in currentList.indices) {
            currentList.removeAt(id)
            userList.value = currentList
            true
        } else {
            false
        }
    }

    fun getUserById(id: Int): User? {
        val currentList = userList.value ?: return null
        return if (id in currentList.indices) {
            currentList[id]
        } else {
            null
        }
    }

}