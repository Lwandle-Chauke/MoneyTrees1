package com.example.moneytrees1.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moneytrees1.R
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.User
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.viewmodels.UserViewModel
import com.example.moneytrees1.viewmodels.UserViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private var isEditing = false
    private lateinit var currentUsername: String
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get the username from SharedPreferences or another storage method
        currentUsername = getCurrentUsername()

        // Initialize ViewModel
        val repository = UserRepository(AppDatabase.getDatabase(this).userDao())
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(repository)
        )[UserViewModel::class.java]

        // Reference UI elements
        val etName = findViewById<EditText>(R.id.et_name)
        val etSurname = findViewById<EditText>(R.id.et_surname)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnEdit = findViewById<Button>(R.id.btn_edit_profile)
        val switchPass = findViewById<Switch>(R.id.switch_show_password)

        // Load current user details
        loadUserData()

        // Setup listeners
        setupPasswordToggle(switchPass, etPassword)
        setupEditProfileButton(btnEdit, etName, etSurname, etEmail, etPassword)
    }

    // Get the saved current username
    private fun getCurrentUsername(): String {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPref.getString("current_username", "") ?: ""
    }

    // Load user info from the database
    private fun loadUserData() {
        userViewModel.getCurrentUser(
            currentUsername,
            onSuccess = { user ->
                runOnUiThread {
                    currentUser = user
                    findViewById<EditText>(R.id.et_name).setText(user.fullName)
                    findViewById<EditText>(R.id.et_surname).setText(user.surname)
                    findViewById<EditText>(R.id.et_email).setText(user.email)
                    findViewById<EditText>(R.id.et_password).setText("********")
                }
            },
            onFailure = { error ->
                runOnUiThread {
                    showToast(error)
                }
            }
        )
    }

    // Toggle password visibility
    private fun setupPasswordToggle(switchPass: Switch, etPassword: EditText) {
        switchPass.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT
            } else {
                etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.length())  // Keep cursor at the end
        }
    }

    // Set up the edit button to toggle between edit and save mode
    private fun setupEditProfileButton(
        btnEdit: Button,
        etName: EditText,
        etSurname: EditText,
        etEmail: EditText,
        etPassword: EditText
    ) {
        btnEdit.setOnClickListener {
            if (isEditing) {
                // Save profile changes
                saveProfileChanges(btnEdit, etName, etSurname, etEmail, etPassword)
            } else {
                // Switch to editing mode
                enterEditMode(btnEdit, etName, etSurname, etEmail, etPassword)
            }
        }
    }

    // Switch to edit mode
    private fun enterEditMode(
        btnEdit: Button,
        etName: EditText,
        etSurname: EditText,
        etEmail: EditText,
        etPassword: EditText
    ) {
        isEditing = true
        btnEdit.text = "Save"
        etName.isEnabled = true
        etSurname.isEnabled = true
        etEmail.isEnabled = true
        etPassword.isEnabled = true
    }

    // Exit edit mode
    private fun exitEditMode(btnEdit: Button, etPassword: EditText) {
        isEditing = false
        btnEdit.text = "Edit"
        etPassword.isEnabled = false
    }

    // Save profile changes
    private fun saveProfileChanges(
        button: Button,
        nameField: EditText,
        surnameField: EditText,
        emailField: EditText,
        passwordField: EditText
    ) {
        val newName = nameField.text.toString().trim()
        val newSurname = surnameField.text.toString().trim()
        val newEmail = emailField.text.toString().trim()
        val newPassword = passwordField.text.toString()

        if (newName.isEmpty() || newSurname.isEmpty() || newEmail.isEmpty()) {
            showToast("Please fill all required fields")
            return
        }

        userViewModel.updateProfile(
            currentUsername,
            newName,
            newSurname,
            newEmail,
            newPassword,
            onSuccess = {
                runOnUiThread {
                    exitEditMode(button, passwordField)
                    showToast("Profile updated successfully")
                    if (currentUser?.email != newEmail) {
                        updateCurrentUsername(newEmail)
                    }
                    loadUserData()
                }
            },
            onFailure = { error ->
                runOnUiThread {
                    showToast(error)
                }
            }
        )
    }

    // Show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Update the current username in SharedPreferences
    private fun updateCurrentUsername(newUsername: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("current_username", newUsername)
            apply()
        }
    }
}
