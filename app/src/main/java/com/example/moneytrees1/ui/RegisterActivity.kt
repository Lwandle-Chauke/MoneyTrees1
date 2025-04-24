package com.example.moneytrees1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

        // Initialize ViewModel  <-- RIGHT HERE
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

        // Reset error states
        binding.etFullName.error = null
        binding.etSurname.error = null
        binding.etUsername.error = null
        binding.etEmail.error = null
        binding.etPassword.error = null
        binding.etRepeatPassword.error = null

        when {
            fullName.isEmpty() -> {
                binding.etFullName.error = "Full name is required"
                binding.etFullName.requestFocus()
            }
            surname.isEmpty() -> {
                binding.etSurname.error = "Surname is required"
                binding.etSurname.requestFocus()
            }
            username.isEmpty() -> {
                binding.etUsername.error = "Username is required"
                binding.etUsername.requestFocus()
            }
            email.isEmpty() -> {
                binding.etEmail.error = "Email is required"
                binding.etEmail.requestFocus()
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.etEmail.error = "Invalid email format"
                binding.etEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password is required"
                binding.etPassword.requestFocus()
            }
            password.length < 6 -> {
                binding.etPassword.error = "Password must be at least 6 characters"
                binding.etPassword.requestFocus()
            }
            password != repeatPassword -> {
                binding.etRepeatPassword.error = "Passwords don't match"
                binding.etRepeatPassword.requestFocus()
            }
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
        binding.btnRegister.isEnabled = false // Disable button
        binding.progressBar.visibility = View.VISIBLE // Show progress

        userViewModel.registerUser(
            user = user,
            onSuccess = {
                Log.i(TAG, "Registration successful for ${user.username}")
                if (!isFinishing && !isDestroyed) {
                    binding.progressBar.visibility = View.GONE // Hide progress
                    showToast("Registration successful!")
                    navigateToLogin()
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Registration failed: $error")
                if (!isFinishing && !isDestroyed) {
                    binding.progressBar.visibility = View.GONE // Hide progress
                    binding.btnRegister.isEnabled = true // Re-enable button
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