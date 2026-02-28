package com.example.usercatalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    //Единственный MutableLiveData, который хранит список пользователей
    private val userList = MutableLiveData<MutableList<User>>(mutableListOf())


    val users: LiveData<MutableList<User>> = userList

    //метод для добавления пользователя
    fun addUser(user: User) {
        val currentList = userList.value ?: mutableListOf()
        currentList.add(user)
        userList.value = currentList  //обновляем LiveData
    }
}