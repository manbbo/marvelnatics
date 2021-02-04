package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.splash

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login.LoginActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuth = FirebaseAuth.getInstance()

        window.statusBarColor = ContextCompat.getColor(this, R.color.primaryBlue)

        val logo : ImageView = iv_splash_logo

        // ZOOM IN
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in_fade_out))

        val user = mAuth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent? = if (user != null)
                Intent(this, MainActivity::class.java)
            else
                Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}