package com.example.fitnesstracker.Login_SignUp

import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.ForgotPassword.ForgottenPassword
import com.example.fitnesstracker.MainActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.databinding.ActivityLogBinding
import com.example.fitnesstracker.setup_pages.NavData
import com.example.fitnesstracker.setup_pages.SharedPrefHelper
import com.example.fitnesstracker.setup_pages.getNextActivity
import com.example.fitnesstracker.setup_pages.getUserId
import com.example.fitnesstracker.setup_pages.nav
import com.example.fitnesstracker.setup_pages.saveLoginState
import com.example.fitnesstracker.setup_pages.updateUserField
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.AccessToken
import org.checkerframework.common.returnsreceiver.qual.This
import java.util.concurrent.Executor

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.d("LoginCheck", "Firebase currentUser: $currentUser, isLoggedIn: $isLoggedIn")

        if (isLoggedIn && currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            FirebaseAuth.getInstance().signOut()
            saveLoginState(false)
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, ForgottenPassword::class.java)
            startActivity(intent)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.imgGoogle.setOnClickListener {
            signInWithGoogle()
        }


        callbackManager = CallbackManager.Factory.create()

        binding.imgFacebook.setOnClickListener {
            loginWithFacebook()

        }



        binding.signupText.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgottenPassword::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            Log.d("LoginTest", "Email: $email")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        saveLoginState(this, true)
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            Log.d("logina12", "if-2")

                            getUserId(email) { doc: String ->
                                if (doc.isNotEmpty()) {
                                    Log.d("logina12", "if-1")

                                    Log.d("LoginTest", "User ID: $doc")
                                    db.collection("users").document(doc)
                                        .get()
                                        .addOnSuccessListener { document ->
                                            Log.d("LoginTest", "Firestore Data: ${document.data}")
                                            updateUserField("loggedIn", true, document.toString())

                                            val data =
                                                document.toObject(SharedPrefHelper.User::class.java)
                                                    ?: SharedPrefHelper.User()
                                            Log.d("LoginTest", data.toString())
                                            if (data != null) {
                                                Log.d("logina12", "if2")

                                                SharedPrefHelper(this).saveUserToPrefs(data)
                                                val nextActivity = getNextActivity(data)
                                                if (nextActivity != null) {

                                                    startActivity(
                                                        nav(
                                                            NavData(
                                                                getNextActivity(data),
                                                                this,
                                                                data.id
                                                            )
                                                        )
                                                    )

                                                    saveLoginState(this, true)
                                                    finish()
                                                } else {
                                                    Log.e(
                                                        "LoginTest",
                                                        "Error: Next activity is null!"
                                                    )
                                                }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("fireStore", "Error fetching user data", e)
                                        }
                                } else {
                                    Log.e("fireStore", "Error: User ID is empty!")
                                }
                            }
                        } else {
                            Log.d("logina12", "if6")

                            Toast.makeText(
                                this,
                                "Login Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", isLoggedIn).commit() // 🔥 تحديث فوري
    }

    //google login
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Failed to login ${e.message}")
                Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to login ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //facebooklogin
    private fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(this@LogIn, "Facebook login canceled", Toast.LENGTH_SHORT).show()

            }

            override fun onError(error: FacebookException) {

                Toast.makeText(this@LogIn, "Error", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }}

    //FIngerprint







