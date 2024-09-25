package com.example.ecommerce_mobile_app

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginTextView: TextView = findViewById(R.id.login_txt)

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
        loginTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}