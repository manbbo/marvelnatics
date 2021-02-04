package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.MainActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest

class CadastroActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mainView: View
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_UP = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Init Fireabase
        mAuth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.tb_cadastro)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainView= findViewById(R.id.view_cadastro_main)
        val btnCadastro: Button = findViewById(R.id.btn_cadastrar)
        val btnGoogle: ConstraintLayout = findViewById(R.id.cl_cadastro_comGoogle)
        val btnFacebook: LoginButton = findViewById(R.id.cl_cadastro_Facebook)
        val inputEmail: TextInputLayout = findViewById(R.id.til_cadastro_emailLayout)
        val inputPassword: TextInputLayout = findViewById(R.id.til_cadastro_senhaLayout)
        val inputName: TextInputLayout = findViewById(R.id.til_cadastro_nomeLayout)
        val inputLastName: TextInputLayout = findViewById(R.id.til_cadastro_sobrenomeLayout)

        // Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        btnGoogle.setOnClickListener {
            signIn()
        }

        // Facebook
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

        btnCadastro.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(inputEmail.editText?.text.toString(),
                inputPassword.editText?.text.toString())
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Log.d("Result signup", "createUserWithEmail:success")
                        val user = mAuth.currentUser

                        val progileUpdates = userProfileChangeRequest {
                            displayName = "${inputName.editText?.text} ${inputLastName.editText?.text}"
                        }

                        user!!.updateProfile(progileUpdates)
                        updateUI(user)
                        openHome()
                    } else {
                        Log.w("Result signup", "createUserWithEmail:failure", task.exception);
                        Snackbar.make(mainView, "Authentication failed.", Snackbar.LENGTH_LONG).show()
                        updateUI(null);
                    }
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_UP){
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

    fun openHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_UP)
    }

    fun updateUI(user: FirebaseUser?){

    }
}