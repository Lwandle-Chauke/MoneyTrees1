package com.example.moneytrees1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moneytrees1.data.User
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.utils.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun loginUser(
        username: String,
        password: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByEmail(username)
                if (user != null && PasswordUtils.verifyPassword(password, user.password)) {
                    onSuccess(user)
                } else {
                    onFailure("Invalid username or password")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "An error occurred")
            }
        }
    }

    fun registerUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val existingUser = repository.getUserByEmail(user.email)
                if (existingUser == null) {
                    repository.insertUser(user)
                    onSuccess()
                } else {
                    onFailure("User already exists")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Registration failed")
            }
        }
    }

    fun getCurrentUser(
        username: String,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByEmail(username)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure("User not found")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "An error occurred")
            }
        }
    }

    fun updateProfile(
        currentUsername: String,
        fullName: String,
        surname: String,
        email: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByEmail(currentUsername)
                if (user != null) {
                    val updatedUser = user.copy(
                        fullName = fullName,
                        surname = surname,
                        email = email,
                        password = if (newPassword.isNotEmpty()) PasswordUtils.hashPassword(newPassword) else user.password
                    )
                    repository.updateUser(updatedUser)
                    onSuccess()
                } else {
                    onFailure("User not found")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "An error occurred")
            }
        }
    }
}

