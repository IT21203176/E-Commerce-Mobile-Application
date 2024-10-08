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
import java.text.DecimalFormat

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkoutBtn : Button = findViewById(R.id.checkout_btn)

        // Retrieve the data from the Intent
        val subTotal = intent.getDoubleExtra("SUB_TOTAL", 0.0)
        val deliveryFee = intent.getDoubleExtra("DELIVERY_FEE", 0.0)
        val tax = intent.getDoubleExtra("TAX", 0.0)
        val total = intent.getDoubleExtra("TOTAL", 0.0)

        // Format the values to display
        val decimalFormat = DecimalFormat("#,###.00")
        binding.subTotAmountTxt.text = "LKR. ${decimalFormat.format(subTotal)}"
        binding.DeliveryAmountTxt.text = "LKR. ${decimalFormat.format(deliveryFee)}"
        binding.TaxAmountTxt.text = "LKR. ${decimalFormat.format(tax)}"
        binding.TotalAmountTxt.text = "LKR. ${decimalFormat.format(total)}"


        checkoutBtn.setOnClickListener {
            showPaymentSuccessPopupBox()
        }

        binding.backBtn1.setOnClickListener {
            onBackPressed() // Navigate back to the previous activity
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