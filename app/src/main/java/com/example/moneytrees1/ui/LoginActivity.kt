package com.example.moneytrees1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moneytrees1.MyApplication
import com.example.moneytrees1.databinding.ActivityLoginBinding
import com.example.moneytrees1.viewmodels.UserViewModel
import com.example.moneytrees1.viewmodels.UserViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    // Logging tag
    private companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Activity created")

        // Initialize ViewModel with factory
        val app = application as MyApplication
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(app.userRepository)
        )[UserViewModel::class.java]

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "Login button clicked")
            attemptLogin()
        }
        binding.tvRegister.setOnClickListener {
            Log.d(TAG, "Register text clicked")
            navigateToRegister()
        }
        binding.tvForgotPassword.setOnClickListener {
            Log.d(TAG, "Forgot password clicked")
            showForgotPassword()
        }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        Log.d(TAG, "Attempting login for user: $username")

        when {
            username.isEmpty() -> {
                binding.etUsername.error = "Username required"
                Log.w(TAG, "Empty username")
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Password required"
                Log.w(TAG, "Empty password")
            }
            password.length < 6 -> {
                binding.etPassword.error = "Minimum 6 characters"
                Log.w(TAG, "Password too short")
            }
            else -> authenticateUser(username, password)
        }
    }

    private fun authenticateUser(username: String, password: String) {
        Log.d(TAG, "Authenticating user: $username")
        binding.btnLogin.isEnabled = false // Disable button during operation

        userViewModel.loginUser(
            username = username,
            password = password,
            onSuccess = { user ->
                Log.i(TAG, "Login successful for user: ${user.username}")
                if (!isFinishing && !isDestroyed) {
                    showToast("Welcome ${user.fullName}!")
                    navigateToMain()
                }
            },
            onFailure = { error ->
                Log.e(TAG, "Login failed: $error")
                if (!isFinishing && !isDestroyed) {
                    binding.btnLogin.isEnabled = true
                    showToast(error)
                }
            }
        )
    }

    private fun navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity")
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun navigateToRegister() {
        Log.d(TAG, "Navigating to RegisterActivity")
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun showForgotPassword() {
        Log.d(TAG, "Showing forgot password message")
        showToast("Password reset feature coming soon")
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}