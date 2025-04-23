package com.example.moneytrees1.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moneytrees1.R

class MainActivity : AppCompatActivity() {

    // Menu items list (class level variable)
    private val menuItems = listOf(
        MenuItem("Transaction History", TransactionHistoryActivity::class.java),
        MenuItem("Budget Planner", BudgetPlannerActivity::class.java),
        MenuItem("Game", GameActivity::class.java),
        MenuItem("Leaderboard", LeaderboardActivity::class.java),
        MenuItem("Achievements", AchievementsActivity::class.java)
    )

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Edge-to-edge setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initFinancialViews()
        setupNavigationListeners()
    }

    private fun initFinancialViews() {
        // Initialize financial dashboard
        findViewById<TextView>(R.id.financial_health_status).text = "Good"
        findViewById<TextView>(R.id.account_balance).text = "Account Balance: $1000"
        findViewById<TextView>(R.id.budget_status).text = "Budget: On Target"
        findViewById<TextView>(R.id.savings_progress).text = "Savings: 20%"

        // Setup interactive elements
        findViewById<SeekBar>(R.id.goal_seekbar).setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        findViewById<Button>(R.id.add_expense_button).setOnClickListener {
            // TODO: Implement expense entry
            Toast.makeText(this, "Expense entry clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigationListeners() {
        // Bottom navigation bar
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            Toast.makeText(this, "Already on home", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.nav_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Side menu
        findViewById<ImageView>(R.id.nav_menu).setOnClickListener {
            showSideMenu()
        }
    }

    private fun showSideMenu() {
        AlertDialog.Builder(this)
            .setTitle("Menu Options")
            .setItems(menuItems.map { it.title }.toTypedArray()) { _, which ->
                startActivity(Intent(this, menuItems[which].targetActivity))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun enableEdgeToEdge() {
        window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
        window.insetsController?.hide(android.view.WindowInsets.Type.navigationBars())
    }

    // Simple data class for menu items
    data class MenuItem(val title: String, val targetActivity: Class<*>)
}