package com.example.moneytrees1.ui

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneytrees1.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> = ActivityScenarioRule(LoginActivity::class.java)

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

        // Add delay for navigation
        Thread.sleep(1000)

        // Verify navigation by checking if MainActivity's content is displayed
        Espresso.onView(ViewMatchers.withText(R.string.app_name)) // Or any unique view in MainActivity
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}