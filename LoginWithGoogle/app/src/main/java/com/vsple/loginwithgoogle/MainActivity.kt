package com.vsple.loginwithgoogle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        googleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions())

        var button = findViewById<Button>(R.id.btlogin)
        button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Now, you have the user's Google account information (account.email, account.id, etc.)
            // Proceed to exchange this information for OAuth 2.0 credentials.
        } catch (e: ApiException) {
            // Handle sign-in failure
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(DriveScopes.DRIVE_FILE)
            .build()
    }

    private fun initializeDriveClient(signInAccount: GoogleSignInAccount) {
        driveClient = Drive.getDriveClient(activity.applicationContext, signInAccount)
        driveResourceClient = Drive.getDriveResourceClient(activity.applicationContext, signInAccount)
        serviceListener?.loggedIn()
    }
}