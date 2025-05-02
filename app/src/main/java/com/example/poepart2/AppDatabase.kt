package com.example.signup_screen  // ✅ match this to your package

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.signup_screen.User      // ✅ This is the line you were missing
import com.example.signup_screen.UserDao  // ✅ Also ensure this is included

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
