package com.rafaelfelipeac.mountains.database.user

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDAO: UserDAO) {

    fun getItems(): LiveData<List<User>> {
        return userDAO.getAll()
    }

    fun getItem(userId: Long): LiveData<User> {
        return userDAO.get(userId)
    }

    fun save(user: User): Long {
        return userDAO.save(user)
    }

    fun delete(user: User) {
        return userDAO.delete(user)
    }

    fun getItemByUUI(uui: String): User {
        return userDAO.getByUUI(uui)
    }
}