package com.example.moneytrees1

import android.app.Application
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.BudgetRepository
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.viewmodels.BudgetViewModelFactory
import com.example.moneytrees1.viewmodels.UserViewModelFactory

/**
 * Custom Application class that provides application-wide dependencies.
 * Initializes Room database, repositories, and ViewModel factories using lazy initialization.
 */
class MyApplication : Application() {

    // Database instance (lazy initialized)
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    // Repositories (lazy initialized)
    val userRepository by lazy {
        UserRepository(database.userDao())
    }

    val budgetRepository by lazy {
        BudgetRepository(database.budgetDao())
    }

    // ViewModel Factories (lazy initialized)
    val userViewModelFactory by lazy {
        UserViewModelFactory(userRepository)
    }

    val budgetViewModelFactory by lazy {
        BudgetViewModelFactory(budgetRepository)
    }

    companion object {
        /**
         * Get the application instance in a safe way.
         * @throws IllegalStateException if called before application is created
         */
        fun getInstance(): MyApplication {
            return instance ?: throw IllegalStateException("Application not created yet!")
        }

        private var instance: MyApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // You can add any application-wide initialization here
        // For example: Crash reporting, Analytics, etc.
    }
}