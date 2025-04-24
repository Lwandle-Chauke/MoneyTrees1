package com.example.moneytrees1.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.example.moneytrees1.MyApplication
import com.example.moneytrees1.R
import com.example.moneytrees1.data.Budget
import com.example.moneytrees1.viewmodels.BudgetViewModel
import com.example.moneytrees1.viewmodels.BudgetViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BudgetPlannerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var totalBudgetText: TextView

    private val viewModel: BudgetViewModel by lazy {
        ViewModelProvider(this, BudgetViewModelFactory((application as MyApplication).budgetRepository))
            .get(BudgetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_planner)

        progressBar = findViewById(R.id.budgetProgressBar)
        progressText = findViewById(R.id.progressPercentageText)
        totalBudgetText = findViewById(R.id.totalBudgetAmount)

        setupTextWatchers()
        setupObservers()
        setupNavigation()

        findViewById<Button>(R.id.btnSaveBudget).setOnClickListener {
            saveBudget()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.currentBudget.collect { budget ->
                budget?.let { updateUI(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.totalSpent.collect { total ->
                totalBudgetText.text = "R ${"%.2f".format(total)}"
            }
        }

        lifecycleScope.launch {
            viewModel.progressPercentage.collect { percentage ->
                progressBar.progress = percentage
                progressText.text = "$percentage% spent"
            }
        }
    }

    private fun updateUI(budget: Budget) {
        findViewById<EditText>(R.id.budgetAmount).setText(budget.budgetAmount.toString())
        findViewById<EditText>(R.id.groceriesBudget).setText(budget.groceriesAmount.toString())
        findViewById<EditText>(R.id.transportBudget).setText(budget.transportAmount.toString())
        findViewById<EditText>(R.id.entertainmentBudget).setText(budget.entertainmentAmount.toString())
        findViewById<EditText>(R.id.minimumGoal).setText(budget.minimumGoal.toString())
        findViewById<EditText>(R.id.maximumGoal).setText(budget.maximumGoal.toString())

        if (budget.budgetType == "Monthly") {
            findViewById<RadioButton>(R.id.btnMonthly).isChecked = true
        } else {
            findViewById<RadioButton>(R.id.btnWeekly).isChecked = true
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateAndUpdate()
            }
        }

        findViewById<EditText>(R.id.groceriesBudget).addTextChangedListener(textWatcher)
        findViewById<EditText>(R.id.transportBudget).addTextChangedListener(textWatcher)
        findViewById<EditText>(R.id.entertainmentBudget).addTextChangedListener(textWatcher)
        findViewById<EditText>(R.id.budgetAmount).addTextChangedListener(textWatcher)
    }

    private fun calculateAndUpdate() {
        val groceries = findViewById<EditText>(R.id.groceriesBudget).text.toString().toDoubleOrNull() ?: 0.0
        val transport = findViewById<EditText>(R.id.transportBudget).text.toString().toDoubleOrNull() ?: 0.0
        val entertainment = findViewById<EditText>(R.id.entertainmentBudget).text.toString().toDoubleOrNull() ?: 0.0
        val budgetAmount = findViewById<EditText>(R.id.budgetAmount).text.toString().toDoubleOrNull() ?: 0.0

        val totalSpent = groceries + transport + entertainment
        totalBudgetText.text = "R ${"%.2f".format(totalSpent)}"

        if (budgetAmount > 0) {
            val percentage = ((totalSpent / budgetAmount) * 100).coerceAtMost(100.0).toInt()
            progressBar.progress = percentage
            progressText.text = "$percentage% spent"
        }
    }

    private fun saveBudget() {
        val budgetType = if (findViewById<RadioButton>(R.id.btnMonthly).isChecked) "Monthly" else "Weekly"
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

        viewModel.saveBudget(budget)
        Toast.makeText(this, "Budget saved!", Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
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
    }
}