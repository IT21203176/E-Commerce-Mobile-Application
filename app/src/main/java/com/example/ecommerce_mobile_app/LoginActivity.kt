package com.example.ecommerce_mobile_app

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ecommerce_mobile_app.Model.UserLoginModel
import com.example.ecommerce_mobile_app.databinding.ActivityLoginBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signinButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        val registerTextView: TextView = findViewById(R.id.register_txt)

        // Set the text from the string resource with HTML tags
        registerTextView.text = Html.fromHtml(getString(R.string.signup_choice))

        // Create a SpannableString from the parsed HTML string
        val spannableString = SpannableString(registerTextView.text)

        // Create a clickable span for "Register Now"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navigate to RegisterActivity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        // Find the start and end of "Register Now" in the string
        val start = spannableString.toString().indexOf("Sign Up for Free")
        val end = start + "Sign Up for Free".length

        // Apply the clickable span to the "Register Now" part of the text
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the modified spannable string to the TextView
        registerTextView.text = spannableString

        // Enable movement method to make the link clickable
        registerTextView.movementMethod = LinkMovementMethod.getInstance()

        // Set OnClickListener for the sign-in button
        binding.signinbtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, ProductLActivity::class.java)
            startActivity(intent)
        }

        // Initialize the views
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.pwd_input)
        signinButton = findViewById(R.id.signinbtn)

        // Set the click listener for the sign-in button
        signinButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (validateInputs(email, password)) {
                loginUser(UserLoginModel(email, password))
            }
        }

        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Validate email and update the icon accordingly
                val emailText = s.toString()
                if (isValidEmail(emailText)) {
                    // Set the drawableEnd icon to green if the email is valid
                    val greenIcon: Drawable? = ContextCompat.getDrawable(this@LoginActivity, R.drawable.done_icon_green)
                    emailInput.setCompoundDrawablesWithIntrinsicBounds(null, null, greenIcon, null)
                } else {
                    // Set the drawableEnd icon to red if the email is invalid
                    val redIcon: Drawable? = ContextCompat.getDrawable(this@LoginActivity, R.drawable.done_icon_red)
                    emailInput.setCompoundDrawablesWithIntrinsicBounds(null, null, redIcon, null)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }
        })

        var isPasswordVisible = false

        passwordInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the drawableEnd was clicked
                if (event.rawX >= (passwordInput.right - passwordInput.compoundDrawables[2].bounds.width())) {
                    // Toggle password visibility
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        // Show password
                        passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        val visibleIcon = ContextCompat.getDrawable(this, R.drawable.password_icon)
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(null, null, visibleIcon, null)
                    } else {
                        // Hide password
                        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        val hiddenIcon = ContextCompat.getDrawable(this, R.drawable.hidden)
                        passwordInput.setCompoundDrawablesWithIntrinsicBounds(null, null, hiddenIcon, null)
                    }
                    // Move the cursor to the end of the text
                    passwordInput.setSelection(passwordInput.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

    }

    private fun validateInputs(email: String, password: String): Boolean {
        // Simple validation for email and password
        return when {
            email.isEmpty() -> {
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    /*private fun loginUser(userLogin: UserLoginModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Make the API call to login the user
                val response: LoginResponseModel = RetrofitClient.apiService.loginUser(userLogin)

                // Handle the response in the main thread
                withContext(Dispatchers.Main) {
                    // Save token and user data, and navigate to the next screen
                    val token = response.token
                    val user = response.user // This will have all fields defined in LoginUserModel

                    // Optionally save token in shared preferences or navigate to another activity
                    // For example, navigating to a ProductActivity
                    Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, ProductLActivity::class.java).apply {
                        putExtra("USER_ID", user.id)
                        putExtra("FIRST_NAME", user.first_Name) // Assuming user has firstName property
                        putExtra("LAST_NAME", user.last_Name)   // Assuming user has lastName property
                        putExtra("EMAIL", user.email)
                        putExtra("NIC", user.nic)
                        putExtra("ADDRESS", user.address)
                    }
                    startActivity(intent)
                    finish() // Close the LoginActivity
                }
            } catch (e: Exception) {
                // Handle any errors during login
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/

    private fun loginUser(userLogin: UserLoginModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the suspend function
                val response: LoginResponseModel = RetrofitClient.apiService.loginUser(userLogin)

                // Handle the response in the main thread
                withContext(Dispatchers.Main) {
                    // Save token and user data in SharedPreferences
                    val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("token", response.token) // Save the token
                    editor.putString("userId", response.user.id) // Save the user ID
                    editor.putString("firstName", response.user.first_Name)
                    editor.putString("lastName", response.user.last_Name)
                    editor.apply() // Save changes

                    // Log the saved user ID
                    Log.d("LoginActivity", "Saved token: ${response.token}")
                    Log.d("LoginActivity", "User ID saved: ${response.user.id}")
                    Log.d("LoginActivity", "Saved firstName: ${response.user.first_Name}")
                    Log.d("LoginActivity", "Saved lastName: ${response.user.last_Name}")

                    // Show a success message
                    Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()

                    // Prepare intent for the next activity
                    val intent = Intent(this@LoginActivity, ProductLActivity::class.java).apply {
                        putExtra("USER_ID", response.user.id)
                        putExtra("FIRST_NAME", response.user.first_Name) // Assuming user has first_Name property
                        putExtra("LAST_NAME", response.user.last_Name)   // Assuming user has last_Name property
                        putExtra("EMAIL", response.user.email)
                        putExtra("NIC", response.user.nic)
                        putExtra("ADDRESS", response.user.address)
                    }
                    //startActivity(intent)
                    //finish() // Close the LoginActivity
                    // Check if user ID and token are not null or empty
                    if (!response.user.id.isNullOrEmpty() && !response.token.isNullOrEmpty()) {
                        startActivity(intent)
                        finish() // Close the LoginActivity
                    } else {
                        Toast.makeText(this@LoginActivity, "User ID or token not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle any errors during login
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}