package com.example.moneytrees1.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.moneytrees1.MyApplication
import com.example.moneytrees1.R
import com.example.moneytrees1.data.User
import com.example.moneytrees1.utils.PasswordUtils
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> = ActivityScenarioRule(LoginActivity::class.java)

    private val testUser = User(
        fullName = "Test User",
        surname = "Test",
        username = "testuser",
        email = "test@example.com",
        password = PasswordUtils.hashPassword("testpassword")
    )

    @Before
    fun setup() = runBlocking {
        // Ensure test user exists before login tests
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApplication
        app.userRepository.insertUser(testUser)
    }

    @After
    fun cleanup() = runBlocking {
        // Clean up test user
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApplication
        app.userDao.deleteUser(testUser.username)
    }

    @Test
    fun testLoginButton_ShowsErrorWhenEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .check(ViewAssertions.matches(
                ViewMatchers.hasErrorText("Username required")
            ))
    }

    @Test
    fun testSuccessfulLogin_NavigatesToMain() {
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.typeText(testUser.username))
        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.typeText("testpassword"))

        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testInvalidCredentials_ShowsError() {
        Espresso.onView(ViewMatchers.withId(R.id.etUsername))
            .perform(ViewActions.typeText("nonexistent"))
        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .perform(ViewActions.typeText("wrongpassword"))

        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText("Invalid username or password"))
            .inRoot(ToastMatcher())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}