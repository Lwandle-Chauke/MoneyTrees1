package com.example.moneytrees1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moneytrees1.MyApplication
import com.example.moneytrees1.databinding.ActivityRegisterBinding
import com.example.moneytrees1.data.User
import com.example.moneytrees1.utils.PasswordUtils
import com.example.moneytrees1.viewmodels.UserViewModel
import com.example.moneytrees1.viewmodels.UserViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Activity created")

        // Initialize ViewModel
        val app = application as MyApplication
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(app.userRepository)
        )[UserViewModel::class.java]

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            Log.d(TAG, "Register button clicked")
            attemptRegistration()
        }
        binding.tvLogin.setOnClickListener {
            Log.d(TAG, "Login text clicked")
            navigateToLogin()
        }
    }

    private fun attemptRegistration() {
        val fullName = binding.etFullName.text.toString().trim()
        val surname = binding.etSurname.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val repeatPassword = binding.etRepeatPassword.text.toString()

        Log.d(TAG, "Attempting registration for $username")

        when {
            fullName.isEmpty() -> showError("Full name is required")
            surname.isEmpty() -> showError("Surname is required")
            username.isEmpty() -> showError("Username is required")
            email.isEmpty() -> showError("Email is required")
            password.isEmpty() -> showError("Password is required")
            password != repeatPassword -> showError("Passwords don't match")
            password.length < 6 -> showError("Password must be at least 6 characters")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showError("Invalid email format")
            else -> {
                val hashedPassword = PasswordUtils.hashPassword(password)
                val user = User(
                    fullName = fullName,
                    surname = surname,
                    username = username,
                    email = email,
                    password = hashedPassword
                )
                registerUser(user)
            }
        }
    }

    private fun registerUser(user: User) {
        binding.btnRegister.isEnabled = false // Disable button during registration

        userViewModel.registerUser(
            user = user,
            onSuccess = {
                Log.i(TAG, "Registration successful for ${user.username}")
                if (!isFinishing && !isDestroyed) {
                    showToast("Registration successful!")
                    navigateToLogin()
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Registration failed: $error")
                if (!isFinishing && !isDestroyed) {
                    binding.btnRegister.isEnabled = true
                    showError(error)
                }
            }
        )
    }

    private fun navigateToLogin() {
        Log.d(TAG, "Navigating to LoginActivity")
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showError(message: String) {
        binding.tvRegisterSubtitle.apply {
            text = message
            setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}