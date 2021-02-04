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
import com.facebook.*
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 6584
    private var EMAIL = "email"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainView: View

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init Firebase
        mAuth = FirebaseAuth.getInstance()

        mainView = findViewById(R.id.view_login_main)
        val btnCadastro: TextView = findViewById(R.id.tv_login_cadastreSe)
        val btnLogin: TextView = findViewById(R.id.btn_login)
        val btnFacebook: LoginButton = findViewById(R.id.btn_Facebook)
        val btngoogle: ConstraintLayout = findViewById(R.id.entrar_com_google)
        val inputEmail: TextInputLayout = findViewById(R.id.til_login_emailLayout)
        val inputPassword: TextInputLayout = findViewById(R.id.til_login_passwordLayout)

        // Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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
            mAuth.signInWithEmailAndPassword(
                inputEmail.editText?.text.toString(),
                inputPassword.editText?.text.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Result signin", "signinWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user)
                        openHome()
                    } else {
                        Log.w("Result signup", "signinWithEmail:failure", task.exception);
                        Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG)
                            .show()
                        updateUI(null);
                    }
                }
        }

        btnFacebook.setOnClickListener {
            btnFacebook.setPermissions("email", "public_profile")
            callbackManager = CallbackManager.Factory.create()

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {

                    override fun onSuccess(loginResult: LoginResult?) {
                        Log.i("Status", "Entrou")
                        handlerFacebookAccessToken(loginResult!!.accessToken)
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
                Toast.makeText(this, "Autenticado com: ${account.email}", Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account.idToken!!)
                openHome()
            } catch (e: ApiException){
                Log.w("google_auth", "Google sign in failed", e)
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credentital = GoogleAuthProvider.getCredential(idToken, null)

        mAuth.signInWithCredential(credentital)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    Log.d("Auth result", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                    openHome()
                } else {
                    Log.w("Auth result", "signInWithCredential:failure", task.exception);
                    Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG)
                        .show()
                    updateUI(null);
                }
            }
    }

    private fun handlerFacebookAccessToken(token: AccessToken){
        Log.d("Facebook token", "handlerFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful){
                    Log.d("Auth result", "signInWithFacebook:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                    openHome()
                } else {
                    Log.w("Auth result", "signInWithFacebook:failure", task.exception);
                    Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG)
                        .show()
                    updateUI(null);
                }
            }
    }

    private fun openHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        // Sera usado quando com implementaçao do banco de dados
    }
}


