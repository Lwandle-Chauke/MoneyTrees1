package com.example.moneytrees1

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ActivityScenarioRule<T : Activity>(private val activityClass: Class<T>) : TestRule {
    private var scenario: ActivityScenario<T>? = null

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                scenario = ActivityScenario.launch(activityClass)
                try {
                    base.evaluate()
                } finally {
                    scenario?.close()
                }
            }
        }
    }
}