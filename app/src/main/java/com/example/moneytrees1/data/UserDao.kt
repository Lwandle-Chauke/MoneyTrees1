package com.example.moneytrees1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User entity defining all database operations.
 * Room generates implementation automatically.
 */
@Dao
interface UserDao {
    /* ============ CREATE OPERATIONS ============ */

    /**
     * Inserts a new user into the database
     * @param user User object to insert
     */
    @Insert
    suspend fun insertUser(user: User)

    /* ============ READ OPERATIONS ============ */

    /**
     * Finds user by username (case sensitive)
     * @param username Username to search
     * @return User object or null
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    /**
     * Finds user by email (case sensitive)
     * @param email Email to search
     * @return User object or null
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Gets all users (for debugging/admin purposes only)
     * @return Flow emitting list of all users
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    /* ============ UPDATE OPERATIONS ============ */

    /**
     * Updates existing user
     * @param user Updated user object
     */
    @Update
    suspend fun updateUser(user: User)

    /* ============ VALIDATION OPERATIONS ============ */

    /**
     * Checks if username exists
     * @param username Username to check
     * @return Boolean indicating existence
     */
    @Query("SELECT EXISTS(SELECT * FROM users WHERE username = :username)")
    suspend fun usernameExists(username: String): Boolean

    /**
     * Checks if email exists
     * @param email Email to check
     * @return Boolean indicating existence
     */
    @Query("SELECT EXISTS(SELECT * FROM users WHERE email = :email)")
    suspend fun emailExists(email: String): Boolean

    /* ============ DELETE OPERATIONS ============ */

    /**
     * Deletes user by username
     * @param username Username to delete
     */
    @Query("DELETE FROM users WHERE username = :username")
    suspend fun deleteUser(username: String)
}