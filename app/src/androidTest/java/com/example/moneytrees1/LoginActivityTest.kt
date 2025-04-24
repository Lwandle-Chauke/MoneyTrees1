package com.example.moneytrees1.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneytrees1.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var scenario: ActivityScenario<LoginActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun testLoginButton_ShowsErrorWhenEmpty() {
        // Click login with empty fields
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        // Check error messages
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .check(ViewAssertions.matches(
                ViewMatchers.hasErrorText("Username required")
            ))
    }

    @Test
    fun testSuccessfulLogin_NavigatesToMain() {
        // Enter test credentials
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.typeText("test"))
        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.typeText("password123"))

        // Close keyboard and click login
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        // Verify navigation (assuming MainActivity has a distinctive view)
        Espresso.onView(ViewMatchers.withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun cleanup() {
        scenario.close()
    }
}