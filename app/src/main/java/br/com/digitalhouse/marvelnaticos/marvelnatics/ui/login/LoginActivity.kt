package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.cadastro.CadastroActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

class LoginActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private var EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnCadastro: TextView = findViewById(R.id.tv_login_cadastreSe)
        val btnLogin: TextView = findViewById(R.id.btn_login)
        val btnFacebook: LoginButton = findViewById(R.id.btn_Facebook)

        btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnLogin.setOnClickListener {
            openHome()
        }

        btnFacebook.setOnClickListener {
            btnFacebook.setReadPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {

                    override fun onSuccess(loginResult: LoginResult?) {
                        Log.i("Status", "Entrou")
                        val graph =
                            GraphRequest.newMeRequest(loginResult?.accessToken) { obj, response ->
                                try {
                                    if (obj.has("id")) {
                                        openHome()
                                        Toast.makeText(getApplicationContext(), "Autenticado pelo facebook", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(getApplicationContext(), "Ocorreu um erro inesperado, tente mais tarde", Toast.LENGTH_LONG).show()
                                }
                            }

                        val param = Bundle()
                        param.putString("fields", "name, email, id, picture.type(large)")
                        graph.parameters = param
                        graph.executeAsync()
                    }

                    override fun onCancel() {
                        Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro inesperado, tente mais tarde", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}


