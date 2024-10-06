package com.example.ecommerce_mobile_app

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_mobile_app.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkoutBtn : Button = findViewById(R.id.checkout_btn)

        checkoutBtn.setOnClickListener {
            showPaymentSuccessPopupBox()
        }
    }

    private fun showPaymentSuccessPopupBox() {
        val dialog = Dialog(this)
        val dialogView : View = LayoutInflater.from(this).inflate(R.layout.custom_payment_successful_box, null)

        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val backToShopBtn: Button = dialogView.findViewById(R.id.checkout_btn)

        backToShopBtn.setOnClickListener {
            dialog.dismiss()

            val intent = Intent(this, ProductLActivity::class.java)
            startActivity(intent)
        }

        dialog.show()

    }

}