package com.rafaelfelipeac.mountains.database.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelfelipeac.mountains.models.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun get(userId: Long): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: User): Long

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM user WHERE uui = :uui")
    fun getByUUI(uui: String): User
}