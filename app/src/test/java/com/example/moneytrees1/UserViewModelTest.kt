package com.example.moneytrees1.viewmodels

import com.example.moneytrees1.data.User
import com.example.moneytrees1.data.UserRepository
import com.example.moneytrees1.utils.PasswordUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private lateinit var viewModel: UserViewModel
    private val mockRepo: UserRepository = mock()

    @Before
    fun setup() {
        viewModel = UserViewModel(mockRepo)
    }

    @Test
    fun `login with valid credentials succeeds`() = runTest {
        // Arrange
        val testUser = User(
            username = "test",
            password = PasswordUtils.hashPassword("password123"),
            fullName = "Test User",
            surname = "Test",
            email = "test@example.com"
        )
        whenever(mockRepo.getUserByUsername("test")).thenReturn(testUser)

        // Act & Assert
        viewModel.loginUser(
            username = "test",
            password = "password123",
            onSuccess = { user -> assertEquals("test", user.username) },
            onFailure = { fail("Should not fail") }
        )
    }

    @Test
    fun `register new user succeeds`() = runTest {
        // Arrange
        val newUser = User(
            username = "newuser",
            password = "hashedPassword",
            fullName = "New",
            surname = "User",
            email = "new@example.com"
        )
        whenever(mockRepo.getUserByUsername("newuser")).thenReturn(null)

        // Act & Assert
        viewModel.registerUser(
            user = newUser,
            onSuccess = { assertTrue(true) },
            onFailure = { fail("Should not fail") }
        )

        // Verify repo was called
        verify(mockRepo).insertUser(newUser)
    }
}