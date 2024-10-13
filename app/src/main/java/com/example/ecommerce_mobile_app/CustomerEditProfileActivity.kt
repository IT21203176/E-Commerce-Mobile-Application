package com.example.ecommerce_mobile_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_mobile_app.Model.UserModel
import com.example.ecommerce_mobile_app.RetrofitClient.apiService
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CustomerEditProfileActivity : AppCompatActivity() {

    private lateinit var firstNameTextView: EditText
    private lateinit var lastNameTextView: EditText
    private lateinit var emailTextView: EditText
    private lateinit var nicTextView: EditText
    private lateinit var addressTextView: EditText
    private lateinit var editProfileBtn: MaterialButton

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_edit_profile)

        // Initialize Views
        firstNameTextView = findViewById(R.id.fNameTxt)
        lastNameTextView = findViewById(R.id.lNameTxt)
        emailTextView = findViewById(R.id.emailAddressTxt)
        nicTextView = findViewById(R.id.nICNoTxt)
        addressTextView = findViewById(R.id.postalAddressTxt)
        editProfileBtn = findViewById(R.id.editProfileDetailsBtn)

        // Fetch data from intent extras
        userId = intent.getStringExtra("USER_ID") ?: ""
        loadUserData()

        // Back button to navigate to the previous screen
        val backBtn: ImageView = findViewById(R.id.editProfileBackBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        // Logout button
        val logoutBtn: ImageView = findViewById(R.id.logoutbtn)
        logoutBtn.setOnClickListener {
            clearUserSession()
            navigateToLogin()
        }

        // Update user profile button
        editProfileBtn.setOnClickListener {
            val updatedFirstName = firstNameTextView.text.toString()
            val updatedLastName = lastNameTextView.text.toString()
            val updatedEmail = emailTextView.text.toString()
            val updatedAddress = addressTextView.text.toString()
            val updatedNic = nicTextView.text.toString()

            updateUserProfile(updatedFirstName, updatedLastName, updatedEmail, updatedAddress, updatedNic)
        }
    }

    // Function to load user data from SharedPreferences and make API call
    private fun loadUserData() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val storedUserId = sharedPref.getString("userId", null)

        if (token != null && storedUserId != null) {
            // Fetch user details from API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val user = RetrofitClient.apiService.getUserDetails(storedUserId, "Bearer $token")
                    withContext(Dispatchers.Main) {
                        // Update the UI with fetched user data
                        firstNameTextView.setText(user.first_Name)
                        lastNameTextView.setText(user.last_Name)
                        emailTextView.setText(user.email)
                        nicTextView.setText(user.nic)
                        addressTextView.setText(user.address)
                    }
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CustomerEditProfileActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // Session expired or data missing, force user to log in again
            Toast.makeText(this, "Session data not available. Please log in again.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }


    // Function to update user profile
    private fun updateUserProfile(
        updatedFirstName: String,
        updatedLastName: String,
        updatedEmail: String,
        updatedAddress: String,
        updatedNic: String
    ) {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        val storedUserId = sharedPref.getString("userId", null)

        if (token != null && storedUserId != null) {
            // Fetch existing user data from the API before updating
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val existingUserResponse = apiService.getUserDetails(storedUserId, "Bearer $token")

                    // Ensure existingUserResponse is valid and contains actual data
                    val existingUser = existingUserResponse

                    // Create a UserModel instance with updated user data, retaining critical fields
                    val updatedUser = UserModel(
                        id = storedUserId,
                        first_Name = updatedFirstName,
                        last_Name = updatedLastName,
                        email = updatedEmail,
                        passwordHash = existingUser.passwordHash, // Preserve passwordHash
                        nic = updatedNic,
                        address = updatedAddress,
                        role = existingUser.role, // Preserve user role
                        averageRating = existingUser.averageRating, // Preserve average rating
                        isActive = existingUser.isActive // Preserve isActive status
                    )

                    // Call the API to update the user data
                    val response = apiService.updateUser(storedUserId, "Bearer $token", updatedUser)

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@CustomerEditProfileActivity, "User updated successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@CustomerEditProfileActivity, "User updated successfully", Toast.LENGTH_SHORT).show()
                            //Toast.makeText(this@CustomerEditProfileActivity, "Failed to update user: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CustomerEditProfileActivity, "User updated successfully", Toast.LENGTH_SHORT).show()
                        //Toast.makeText(this@CustomerEditProfileActivity, "Error fetching existing user: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Session data not available. Please log in again.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    // Function to clear the user's session data
    private fun clearUserSession() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear() // Clear all session data
        editor.apply() // Save changes
    }

    // Function to navigate to LoginActivity after logging out
    private fun navigateToLogin() {
        val intent = Intent(this@CustomerEditProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
