package com.example.moneytrees1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a User entity in the Room database.
 *
 * @property id Auto-generated primary key
 * @property fullName User's complete name
 * @property surname User's family name
 * @property username Unique identifier for login
 * @property email User's email address
 * @property password Securely hashed password (never store plain text)
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val surname: String,
    val username: String,
    val email: String,
    val password: String // Always store hashed passwords only
) {
    /**
     * Validates that all required fields contain data
     * @return Boolean indicating if user is valid
     */
    fun isValid(): Boolean {
        return fullName.isNotBlank() &&
                surname.isNotBlank() &&
                username.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank()
    }

    /**
     * Checks if email format is valid using simple pattern matching
     * @return Boolean indicating valid email format
     */
    fun hasValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}