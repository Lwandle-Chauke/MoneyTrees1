package com.example.moneytrees1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneytrees1.data.Budget
import com.example.moneytrees1.data.User

/**
 * The Room database for this app that contains the User and Budget tables.
 *
 * @property version The database version (increment when schema changes)
 * @property entities List of entity classes that belong to this database
 */
@Database(
    entities = [User::class, Budget::class], // Include all your entities here
    version = 1,
    exportSchema = false
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
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * Creates the database if it doesn't already exist.
         *
         * @param context The context to use for creating the database
         * @return The AppDatabase instance
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moneytrees_database" // You can also keep "money_trees_db" if preferred
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
