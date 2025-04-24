package com.example.moneytrees1.ui

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher : TypeSafeMatcher<Root>() {

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        // Get window type from layout parameters
        val type: Int? = try {
            root.windowLayoutParams?.get()?.type
        } catch (e: Exception) {
            null
        }

        // Check if it's a toast window type
        if (type == WindowManager.LayoutParams.TYPE_TOAST ||
            type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {

            // Get window tokens
            val windowToken: IBinder = root.decorView.windowToken
            val appToken: IBinder = root.decorView.applicationWindowToken

            // Return true if tokens match (means it's not contained in another window)
            return windowToken == appToken
        }
        return false
    }

    companion object {
        fun isToast(): ToastMatcher {
            return ToastMatcher()
        }
    }
}