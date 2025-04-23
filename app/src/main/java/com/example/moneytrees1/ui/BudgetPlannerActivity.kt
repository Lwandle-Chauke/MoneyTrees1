package com.example.moneytrees1.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.moneytrees1.R
import com.example.moneytrees1.data.AppDatabase
import com.example.moneytrees1.data.Budget
import com.example.moneytrees1.data.BudgetDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BudgetPlannerActivity : AppCompatActivity() {

    private lateinit var budgetDao: BudgetDao
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_planner)

        progressBar = findViewById(R.id.budgetProgressBar)
        progressText = findViewById(R.id.progressPercentageText)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "moneytree-db"
        ).build()
        budgetDao = db.budgetDao()

        loadLatestBudget() // Load progress when screen opens

        findViewById<ImageView>(R.id.nav_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<ImageView>(R.id.nav_menu).setOnClickListener {
            startActivity(Intent(this, NavigationActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddCategory).setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnSaveBudget).setOnClickListener {
            saveBudget()
        }
    }

    private fun saveBudget() {
        val budgetType = if (findViewById<Button>(R.id.btnMonthly).isPressed) "Monthly" else "Weekly"

        val budgetAmount = findViewById<EditText>(R.id.budgetAmount).text.toString().toDoubleOrNull() ?: 0.0
        val groceriesAmount = findViewById<EditText>(R.id.groceriesBudget).text.toString().toDoubleOrNull() ?: 0.0
        val transportAmount = findViewById<EditText>(R.id.transportBudget).text.toString().toDoubleOrNull() ?: 0.0
        val entertainmentAmount = findViewById<EditText>(R.id.entertainmentBudget).text.toString().toDoubleOrNull() ?: 0.0
        val minGoal = findViewById<EditText>(R.id.minimumGoal).text.toString().toDoubleOrNull() ?: 0.0
        val maxGoal = findViewById<EditText>(R.id.maximumGoal).text.toString().toDoubleOrNull() ?: 0.0

        val budget = Budget(
            budgetType = budgetType,
            budgetAmount = budgetAmount,
            groceriesAmount = groceriesAmount,
            transportAmount = transportAmount,
            entertainmentAmount = entertainmentAmount,
            minimumGoal = minGoal,
            maximumGoal = maxGoal
        )

        CoroutineScope(Dispatchers.IO).launch {
            budgetDao.insertBudget(budget)
            withContext(Dispatchers.Main) {
                loadLatestBudget() // Reload progress bar after saving
                Toast.makeText(this@BudgetPlannerActivity, "Budget saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadLatestBudget() {
        CoroutineScope(Dispatchers.IO).launch {
            val latest = budgetDao.getLastBudget()
            latest?.let { budget ->
                val totalSpent = budget.groceriesAmount + budget.transportAmount + budget.entertainmentAmount
                val percentage = if (budget.budgetAmount > 0) {
                    ((totalSpent / budget.budgetAmount) * 100).coerceAtMost(100.0).toInt()
                } else 0

                withContext(Dispatchers.Main) {
                    progressBar.max = 100
                    ObjectAnimator.ofInt(progressBar, "progress", percentage).setDuration(800).start()
                    progressText.text = "$percentage% spent"
                }
            }
        }
    }
}
