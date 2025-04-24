package com.example.moneytrees1.utils

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordUtils {
    /**
     * Hashes a password using BCrypt
     */
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    /**
     * Verifies a password against a BCrypt hash
     */
    fun verifyPassword(password: String, hash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}