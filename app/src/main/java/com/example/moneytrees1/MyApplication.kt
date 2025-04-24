package com.example.moneytrees1

import android.app.Application
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.data.BudgetRepository

class MyApplication : Application() {
    // Using lazy so the database and repository are only created when needed
    val userDao by lazy { database.userDao() }
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val budgetRepository by lazy { BudgetRepository(database.budgetDao()) }
}