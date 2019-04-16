package com.amishgarg.wartube.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.amishgarg.wartube.FirebaseUtil
import com.amishgarg.wartube.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_email_sign_up.*
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.HashMap
import kotlin.math.log

class WelcomeActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private val RC_SIGN_IN = 2
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val TAG = "WelcomeActivity"
    private val web_client_id = "96270419709-b9tkkuje7t89i55h0pj60ufnitrg55fn.apps.googleusercontent.com"

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        MainActivity.navController.popBackStack()
         findViewById<com.shobhitpuri.custombuttons.GoogleSignInButton>(R.id.sign_in_button).setOnClickListener(this)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(web_client_id)
                .requestEmail()
                .build()
        //web_client id is the real deal

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val signupButton = findViewById<Button>(R.id.email_signup_button)
        signupButton.setOnClickListener {
            MainActivity.navController.navigate(R.id.signup_dest)
        }
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            signIn(email_in.text.toString(), pass_in.text.toString())

        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.getCurrentUser()
        if(currentUser != null)
        {
            Toast.makeText(this, "Verify email and Sign in",
                    Toast.LENGTH_LONG).show()
        }

    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }

        }
    }
    //

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user = mAuth!!.getCurrentUser()

                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Snackbar.make(findViewById(R.id.welcome_layout), "Authentication Failed." + task.exception?.message, Snackbar.LENGTH_SHORT).show()
                updateUI(null)
            }

            // ...
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> signIn()
        }



    }


    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

//        showProgressDialog()

        // [START sign_in_with_email]
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = mAuth!!.currentUser
                eupdateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed." + task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                eupdateUI(null)
            }

            // [START_EXCLUDE]
            if (!task.isSuccessful) {
                Toast.makeText(baseContext, "Authentication failed." + task.exception?.message,
                        Toast.LENGTH_SHORT).show()
            }
//                    hideProgressDialog()
            // [END_EXCLUDE]
        }
        // [END sign_in_with_email]
    }




    private fun validateForm(): Boolean {
        var valid = true

        val email = email_in.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_in.error = "Required."
            valid = false
        } else {
            email_in.error = null
        }

        val password = pass_in.text.toString()
        if (TextUtils.isEmpty(password)) {
            pass_in.error = "Required."
            valid = false
        } else {
            pass_in.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        //progressDialog.hide();

        if (user != null) {

            Log.w(TAG, user.getEmail())
            val s: CharSequence = user.email.toString()
            Snackbar.make(this.findViewById(R.id.welcome_layout), s , Snackbar.LENGTH_SHORT).show()
            val uMap = HashMap<String, Any>()
            uMap["display_name"] = user.displayName.toString()
            uMap["profile_pic"] = user.photoUrl.toString()
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                override fun onComplete(p0: Task<InstanceIdResult>) {
                    if(p0.isSuccessful)
                    {
                        Log.d("FirebaseNotif", p0.result?.token)
                        uMap["token"] = p0.result?.token.toString()
                        databaseReference.child("people").child(user!!.uid).updateChildren(uMap, object: DatabaseReference.CompletionListener
                        {
                            override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                                if (p0 != null) {
                                    Toast.makeText(this@WelcomeActivity,
                                            "Couldn't save user data: " + p0.message,
                                            Toast.LENGTH_LONG).show()
                                }}
                        }
                        )
                    }
                    else
                    {
                        Log.d("FirebaseNotif", p0.exception?.message)
                    }
                }
            })

            if(user.isEmailVerified)
            {
                Toast.makeText(this, "Successfully logged in",
                        Toast.LENGTH_LONG).show()
            }
            finish()
            Log.d("WelcomeActivity", "COde is here")
            //  setContentView(R.layout.)

        } else {
                Toast.makeText(this,"Signed Out",Toast.LENGTH_LONG)
        }
    }

    private fun eupdateUI(user: FirebaseUser?) {

        if (user != null) {

            if(user.isEmailVerified)
            {
                Toast.makeText(this, "Successfully logged in",
                        Toast.LENGTH_LONG).show()
            }
            finish()
            Log.d("WelcomeActivity", "COde is here 2")

        } else {
            Toast.makeText(this,"Signed Out",Toast.LENGTH_LONG)
        }
    }

}
