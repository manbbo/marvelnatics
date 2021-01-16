package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.google.android.gms.auth.api.credentials.IdToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 6584
    private var EMAIL = "email"

    override fun onStart() {
        super.onStart()
        val currentUser: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnCadastro: TextView = findViewById(R.id.tv_login_cadastreSe)
        val btnLogin: TextView = findViewById(R.id.btn_login)
        val btnFacebook: LoginButton = findViewById(R.id.btn_Facebook)
        val btngoogle: ConstraintLayout = findViewById(R.id.entrar_com_google)

        // Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        btngoogle.setOnClickListener {
            signIn()
        }

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
                                        Toast.makeText(applicationContext, "Autenticado pelo facebook", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(applicationContext, "Ocorreu um erro inesperado, tente mais tarde", Toast.LENGTH_LONG).show()
                                }
                            }

                        val param = Bundle()
                        param.putString("fields", "name, email, id, picture.type(large)")
                        graph.parameters = param
                        graph.executeAsync()
                    }

                    override fun onCancel() {
                        Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(applicationContext, "Ocorreu um erro inesperado, tente mais tarde", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("google_auth", "firebaseAuthWithGoogle: " + account.id)
                updateUI(account)
                Toast.makeText(this, "Autenticado com: ${account.email}", Toast.LENGTH_LONG).show()
                openHome()
            } catch (e: ApiException){
                Log.w("google_auth", "Google sign in failed", e)
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: GoogleSignInAccount?){
        // Sera usado quando com implementaçao do banco de dados
    }
}


