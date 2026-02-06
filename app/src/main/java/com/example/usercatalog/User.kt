package com.example.usercatalog

class User (val name: String, val age: Int?) {
    override fun toString(): String {
        return "$name\n $age лет"
    }
}