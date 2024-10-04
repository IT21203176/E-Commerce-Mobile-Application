package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_mobile_app.Model.UserModel
import com.example.ecommerce_mobile_app.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    data class RegisterRequest(
        val newUser: UserModel // Wrap the UserModel inside newUser
    )

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val loginTextView: TextView = findViewById(R.id.login_txt)

        // Set the text from the string resource with HTML tags
        val text = HtmlCompat.fromHtml(getString(R.string.sign_in_choice),HtmlCompat.FROM_HTML_MODE_LEGACY)

        // Create a SpannableString from the parsed HTML string
        val spannableString = SpannableString(text)

        // Create a clickable span for "Sign In"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Navigate to LoginActivity
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        // Find the start and end of "Sign In" in the string
        val start =  spannableString.toString().indexOf("Sign In")
        val end = start + "Sign In".length

        // Apply the clickable span to the "Sign In" part of the text
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the modified spannable string to the TextView
        loginTextView.text = spannableString

        // Enable movement method to make the link clickable
        loginTextView.movementMethod = LinkMovementMethod.getInstance()*/

        // Handle Sign Up button click
        binding.signupBtn.setOnClickListener {
            val firstName = binding.fNameInput.text.toString().trim()
            val lastName = binding.lNameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.pwdInput.text.toString().trim()
            val nic = binding.nicInput.text.toString().trim()
            val address = binding.addressInput.text.toString().trim()

            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || nic.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a UserModel object
            val userModel = UserModel(
                id = "",
                first_Name = firstName,
                last_Name = lastName,
                email = email,
                passwordHash = password, // You might want to hash the password before sending it
                nic = nic,
                address = address,
                role = "0", // Assuming 0 represents a normal user
                averageRating = 0.0,
                isActive = 1
            )

            // Call the API to register the user
            registerUser(userModel)
        }
    }

    /*private fun registerUser(userModel: UserModel) {
        val call = RetrofitClient.apiService.registerUser(userModel)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    // Redirect to another activity (e.g., LoginActivity)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, "Registration Failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun registerUser(userModel: UserModel) {
        val call = RetrofitClient.apiService.registerUser(userModel)
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()

                    // Clear input fields
                    binding.fNameInput.text.clear()
                    binding.lNameInput.text.clear()
                    binding.emailInput.text.clear()
                    binding.pwdInput.text.clear()
                    binding.nicInput.text.clear()
                    binding.addressInput.text.clear()

                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, "Registration Failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("RegisterActivity", "Error: ${t.message}")
            }
        })
    }

}