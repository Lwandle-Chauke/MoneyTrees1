package com.example.moneytrees1.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    // For registration
    suspend fun insertUser(user: User) = userDao.insertUser(user)

    // For login
    suspend fun getUserByUsername(username: String): User? =
        userDao.getUserByUsername(username)

    // For email validation
    suspend fun getUserByEmail(email: String): User? =
        userDao.getUserByEmail(email)

    // For username availability check
    suspend fun usernameExists(username: String): Boolean =
        userDao.usernameExists(username)

    // For email availability check
    suspend fun emailExists(email: String): Boolean =
        userDao.emailExists(email)

    // For profile updates
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // For debugging/admin (optional)
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
}