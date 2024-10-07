package com.example.ecommerce_mobile_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_mobile_app.Model.UserModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class CustomerEditProfileActivity : AppCompatActivity() {

    private lateinit var firstNameTextView: EditText
    private lateinit var lastNameTextView: EditText
    private lateinit var emailTextView: EditText
    private lateinit var nicTextView: EditText
    private lateinit var addressTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_profile)

        // Initialize TextViews
        firstNameTextView = findViewById(R.id.fNameTxt)
        lastNameTextView = findViewById(R.id.lNameTxt)
        emailTextView = findViewById(R.id.emailAddressTxt)
        nicTextView = findViewById(R.id.nICNoTxt)
        addressTextView = findViewById(R.id.postalAddressTxt)

        // Back button to navigate to the previous screen
        val backBtn: ImageView = findViewById(R.id.editProfileBackBtn)
        backBtn.setOnClickListener {
            onBackPressed() // This will navigate back to the previous screen
        }

        // Logout button to log the user out and navigate to the login screen
        val logoutBtn: ImageView = findViewById(R.id.logoutbtn)
        logoutBtn.setOnClickListener {
            clearUserSession() // Clear session data
            navigateToLogin() // Redirect to LoginActivity
        }

        // Load user data
        loadUserData()

        // Edit Profile Button
        val editProfileButton: MaterialButton = findViewById(R.id.editProfileDetailsBtn)
        editProfileButton.setOnClickListener {
            //updateUserProfile()
        }

    }

    private fun loadUserData() {
        // Get the stored user ID and token from SharedPreferences
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val userId = sharedPref.getString("userId", null)

        if (token != null && userId != null) {
            // Fetch user details from the API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val user = RetrofitClient.apiService.getUserDetails(userId, "Bearer $token")
                    withContext(Dispatchers.Main) {
                        // Update the UI with the user details
                        firstNameTextView.setText(user.first_Name)
                        lastNameTextView.setText(user.last_Name)
                        emailTextView.setText(user.email)
                        nicTextView.setText(user.nic)
                        addressTextView.setText(user.address)
                    }
                } catch (e: HttpException) {
                    // Handle any error here, e.g. display a message
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CustomerEditProfileActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Handle case where session data is not available
            Toast.makeText(this, "Session data not available. Please log in again.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    // Function to clear the user's session data from SharedPreferences
    private fun clearUserSession() {
        // Access SharedPreferences where the session data (e.g., JWT token) is stored
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)

        // Clear all stored data such as token, user ID, name, etc.
        val editor = sharedPref.edit()
        editor.clear() // Removes all keys and values
        editor.apply() // Save the changes
    }

    // Function to navigate to LoginActivity after logging out
    private fun navigateToLogin() {
        val intent = Intent(this@CustomerEditProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the back stack
        startActivity(intent)
        finish() // Close the current activity so that the user can't go back to it
    }



}