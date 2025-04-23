package com.example.moneytrees1

import android.app.Application
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.data.BudgetRepository

/**
 * Custom Application class that provides application-wide dependencies.
 * Initializes Room database and repositories using lazy initialization.
 */
class MyApplication : Application() {
    // Using lazy initialization for database and repositories
    // This ensures they're only created when needed and only once

    /**
     * The application-wide database instance.
     * Initialized lazily to avoid blocking the main thread during app startup.
     */
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    /**
     * The application-wide UserRepository instance.
     * Provides access to user-related data operations.
     */
    val userRepository by lazy {
        UserRepository(database.userDao())
    }

    /**
     * The application-wide BudgetRepository instance.
     * Provides access to budget-related data operations.
     */
    val budgetRepository by lazy {
        BudgetRepository(database.budgetDao())
    }
}