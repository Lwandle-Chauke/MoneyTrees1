package com.example.moneytrees1

import android.app.Application
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.BudgetRepository
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.viewmodels.BudgetViewModelFactory
import com.example.moneytrees1.viewmodels.UserViewModelFactory
import timber.log.Timber

/**
 * Custom Application class that initializes global dependencies like Room database,
 * repositories, and ViewModel factories. Also sets up Timber for logging in debug builds.
 */
class MyApplication : Application() {

    // Lazy initialization of Room database
    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    // Lazy initialization of repositories
    val userRepository by lazy {
        UserRepository(database.userDao())
    }

    val budgetRepository by lazy {
        BudgetRepository(database.budgetDao())
    }

    // Lazy initialization of ViewModel factories
    val userViewModelFactory by lazy {
        UserViewModelFactory(userRepository)
    }

    val budgetViewModelFactory by lazy {
        BudgetViewModelFactory(budgetRepository)
    }

    companion object {
        private var instance: MyApplication? = null

        /**
         * Provides a singleton instance of the application.
         * Throws IllegalStateException if accessed before `onCreate()`.
         */
        fun getInstance(): MyApplication {
            return instance ?: throw IllegalStateException("Application not created yet!")
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Set the static instance for global access
        instance = this

        // Initialize Timber logging only in debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.d("Application created")
    }
}
