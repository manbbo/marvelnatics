package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.FirebaseViewModel
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.NetworkViewModel
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.cadastro.CadastroActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.OfflineViewModel
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
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN = 6584
    private var EMAIL = "email"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainView: View

    private val networkViewModel: NetworkViewModel by viewModels()

    val viewModel: OfflineViewModel by viewModels<OfflineViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OfflineViewModel(repo, this@LoginActivity) as T
            }
        }
    }
    val firebaseViewModel: FirebaseViewModel by viewModels()


    override fun onPause() {
        super.onPause()
        networkViewModel.unregisterNetworkListener(this)
    }

    override fun onResume() {
        super.onResume()
        networkViewModel.registerNetworkListener(this)
    }


    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        updateUI(currentUser, true) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init Firebase
        mAuth = FirebaseAuth.getInstance()

        mainView = findViewById(R.id.view_login_main)
        val btnCadastro: TextView = findViewById(R.id.tv_login_cadastreSe)
        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnFacebook: LoginButton = findViewById(R.id.btn_Facebook)
        val btngoogle: ConstraintLayout = findViewById(R.id.entrar_com_google)
        val inputEmail: TextInputLayout = findViewById(R.id.til_login_emailLayout)
        val inputPassword: TextInputLayout = findViewById(R.id.til_login_passwordLayout)

        // Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
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
            if (!inputEmail.editText?.text.isNullOrEmpty() && !inputPassword.editText?.text.isNullOrEmpty()) {
                btnLogin.isEnabled = false
                mAuth.signInWithEmailAndPassword(inputEmail.editText?.text.toString(), inputPassword.editText?.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Result signin", "signinWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user, false) {
                            openHome()
                        }
                    } else {
                        btnLogin.isEnabled = true
                        Log.w("Result signup", "signinWithEmail:failure", task.exception);
                        Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG).show()
                        updateUI(null, false) {}
                    }
                }
            } else {
                Snackbar.make(mainView, "Email and Password cannot be empty", Snackbar.LENGTH_LONG).show()

                openHome()
            }
        }

        btnFacebook.setOnClickListener {
            btnFacebook.setPermissions("email", "public_profile")
            callbackManager = CallbackManager.Factory.create()

            LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult?> {

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

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("google_auth", "firebaseAuthWithGoogle: " + account.id)
                Toast.makeText(this, "Autenticado com: ${account.email}", Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account.idToken!!)
                openHome()
            } catch (e: ApiException) {
                Log.w("google_auth", "Google sign in failed", e)
                Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_LONG).show()
                updateUI(null, false) {}
            }
        } else callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credentital = GoogleAuthProvider.getCredential(idToken, null)

        mAuth.signInWithCredential(credentital).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("Auth result", "signInWithCredential:success")
                val user = mAuth.currentUser
                updateUI(user, false) {
                    openHome()
                }
            } else {
                Log.w("Auth result", "signInWithCredential:failure", task.exception);
                Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG).show()
                updateUI(null, false) {}
            }
        }
    }

    private fun handlerFacebookAccessToken(token: AccessToken) {
        Log.d("Facebook token", "handlerFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("Auth result", "signInWithFacebook:success")
                val user = mAuth.currentUser
                updateUI(user, false) {
                    openHome()
                }
            } else {
                Log.w("Auth result", "signInWithFacebook:failure", task.exception);
                Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG).show()
                updateUI(null, false) {}
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

    // Sera usado quando com implementaçao do banco de dados
    private fun updateUI(user: FirebaseUser?, automatic: Boolean, onDone: Runnable) {
        if (!automatic && user != null) {
            firebaseViewModel.isUserAvaliable.observe(this) { userAvaliable ->
                if (userAvaliable ?: false) {
                    Log.i("LoginActivity", "Carregando dados do usuario...")
                    thread {
                        Tasks.await(firebaseViewModel.loadUserDataToLocal(viewModel))
                        Log.i("LoginActivity", "Dados carregados!")
                        runOnUiThread { onDone.run() }
                    }
                    firebaseViewModel.isUserAvaliable.removeObservers(this)
                }
            }
            firebaseViewModel.setup()
        }
    }
}


