package com.example.signup_screen
// match your package

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: com.example.signup_screen.User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): com.example.signup_screen.User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<com.example.signup_screen.User>
}
