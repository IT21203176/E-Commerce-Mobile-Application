package com.example.ecommerce_mobile_app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatButton

class RateUsDialog(context: Context) : Dialog(context) {

    private var userRate: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rate_us_dialog_layout)

        val rateNowBtn = findViewById<AppCompatButton>(R.id.rateNowBtn)
        val laterBtn = findViewById<AppCompatButton>(R.id.laterBtn)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val ratingImage = findViewById<ImageView>(R.id.ratingImage)

        rateNowBtn.setOnClickListener {
            // Implement functionality here
        }

        laterBtn.setOnClickListener {
            dismiss()
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            when {
                rating <= 1 -> ratingImage.setImageResource(R.drawable.one_star)
                rating <= 2 -> ratingImage.setImageResource(R.drawable.two_star)
                rating <= 3 -> ratingImage.setImageResource(R.drawable.three_star)
                rating <= 4 -> ratingImage.setImageResource(R.drawable.four_star)
                rating <= 5 -> {
                    ratingImage.setImageResource(R.drawable.five_star)
                    animateImage(ratingImage)

                    // Selected rating by user
                    userRate = rating
                }
            }
        }
    }

    private fun animateImage(ratingImage: ImageView) {
        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.fillAfter = true
        scaleAnimation.duration = 200
        ratingImage.startAnimation(scaleAnimation)
    }
}
