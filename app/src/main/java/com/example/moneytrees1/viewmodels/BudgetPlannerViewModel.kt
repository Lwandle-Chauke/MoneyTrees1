package com.example.moneytrees1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneytrees1.data.Budget
import com.example.moneytrees1.data.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository) : ViewModel() {
    private val _currentBudget = MutableStateFlow<Budget?>(null)
    val currentBudget: StateFlow<Budget?> = _currentBudget

    private val _totalSpent = MutableStateFlow(0.0)
    val totalSpent: StateFlow<Double> = _totalSpent

    private val _progressPercentage = MutableStateFlow(0)
    val progressPercentage: StateFlow<Int> = _progressPercentage

    init {
        loadLatestBudget()
    }

    fun loadLatestBudget() {
        viewModelScope.launch(Dispatchers.IO) {
            val budget = repository.getLatestBudget()
            _currentBudget.value = budget
            budget?.let { calculateTotals(it) }
        }
    }

    fun saveBudget(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBudget(budget)
            _currentBudget.value = budget
            calculateTotals(budget)
        }
    }

    private fun calculateTotals(budget: Budget) {
        val spent = budget.groceriesAmount + budget.transportAmount + budget.entertainmentAmount
        _totalSpent.value = spent

        val percentage = if (budget.budgetAmount > 0) {
            ((spent / budget.budgetAmount) * 100).coerceAtMost(100.0).toInt()
        } else 0
        _progressPercentage.value = percentage
    }
}
