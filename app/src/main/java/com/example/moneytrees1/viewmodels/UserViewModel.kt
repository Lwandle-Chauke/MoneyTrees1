package com.example.moneytrees1.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytrees1.data.User
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.utils.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for managing user data operations in a lifecycle-conscious way.
 */
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // Add this companion object for logging
    private companion object {
        const val TAG = "UserViewModel"
    }

    /**
     * Attempts to log in a user with the given credentials.
     */
    fun loginUser(
        username: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByUsername(username)
                if (user != null && PasswordUtils.verifyPassword(password, user.password)) {
                    onSuccess(user)
                } else {
                    onFailure("Invalid username or password")
                }
            } catch (e: Exception) {
                onFailure("Login error: ${e.message}")
            }
        }
    }

    /**
     * Registers a new user after checking username availability.
     */
    fun registerUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = repository.getUserByUsername(user.username)
                if (existingUser != null) {
                    Log.e(TAG, "Username ${user.username} already exists")
                    onFailure("Username already exists")
                } else {
                    repository.insertUser(user)
                    Log.i(TAG, "User ${user.username} registered successfully")
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Registration error for ${user.username}", e)
                onFailure("Registration error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    /**
     * Gets the current user by username and returns it via callback.
     */
    fun getCurrentUser(
        username: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByUsername(username)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure("User not found")
                }
            } catch (e: Exception) {
                onFailure("Error fetching user: ${e.message}")
            }
        }
    }

    /**
     * Updates the user profile with new values.
     */
    fun updateProfile(
        currentUsername: String,
        newName: String,
        newSurname: String,
        newEmail: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = repository.getUserByUsername(currentUsername)
                if (existingUser != null) {
                    val updatedUser = existingUser.copy(
                        fullName = newName,
                        surname = newSurname,
                        email = newEmail,
                        password = newPassword // ✅ NOTE: Should be hashed before calling this
                    )
                    repository.updateUser(updatedUser)
                    onSuccess()
                } else {
                    onFailure("User not found for update.")
                }
            } catch (e: Exception) {
                onFailure("Failed to update profile: ${e.message}")
            }
        }
    }
}
