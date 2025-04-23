package com.example.moneytrees1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for this app that contains the User and Budget tables.
 *
 * @property version The database version (increment when schema changes)
 * @property entities List of entity classes that belong to this database
 */
@Database(
    entities = [User::class, Budget::class], // Include all your entities here
    version = 1,
    exportSchema = false // Set to true if you want to keep schema version history
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides access to the User Data Access Object (DAO)
     * @return UserDao instance
     */
    abstract fun userDao(): UserDao

    /**
     * Provides access to the Budget Data Access Object (DAO)
     * @return BudgetDao instance
     */
    abstract fun budgetDao(): BudgetDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Gets the singleton instance of the database.
         *
         * @param context The application context
         * @return The database instance
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database instance
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "money_trees_db" // Database name
                )
                    .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}