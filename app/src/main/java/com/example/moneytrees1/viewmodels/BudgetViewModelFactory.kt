package com.example.moneytrees1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moneytrees1.data.BudgetRepository

class BudgetViewModelFactory(
    private val repository: BudgetRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BudgetViewModel(repository) as T
    }
}