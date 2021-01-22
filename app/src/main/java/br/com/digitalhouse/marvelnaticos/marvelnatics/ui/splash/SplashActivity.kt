package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.splash

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryBlue))

        val logo : ImageView = iv_splash_logo

        // ZOOM IN
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in_fade_out))

        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}