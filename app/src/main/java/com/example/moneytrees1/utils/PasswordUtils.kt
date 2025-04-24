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
     * Verifies a password against a BCrypt hash.
     * Returns false if either input is blank.
     */
    fun verifyPassword(password: String, hash: String): Boolean {
        if (password.isBlank() || hash.isBlank()) return false
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified
    }
}
