package com.example.ecommerce_mobile_app

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_mobile_app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }
}