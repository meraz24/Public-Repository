package com.passportv3locity.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.passportv3locity.R
import com.passportv3locity.applocal.AppLocalStore
import com.passportv3locity.common.KeyAndLink
import link.magic.android.Magic
import link.magic.android.extension.oauth.oauth
import link.magic.android.extension.oauth.requestConfiguration.OAuthConfiguration
import link.magic.android.extension.oauth.requestConfiguration.OAuthProvider
import link.magic.android.extension.oauth.response.OAuthResponse
import link.magic.android.modules.auth.requestConfiguration.LoginWithMagicLinkConfiguration
import link.magic.android.modules.auth.response.DIDToken
import link.magic.android.modules.wallet.response.ConnectWithUIResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var magic: Magic
    lateinit var bt :Button
    lateinit var et :EditText
    var selectedListIndex: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt = findViewById(R.id.btStart)
        et = findViewById(R.id.etemail)


        var appLocalStore = AppLocalStore(context = this)

        bt.setOnClickListener {
            mcLogin(it)
        }
        magic = Magic(applicationContext, KeyAndLink.MAGIC_LINK_API_KEY)
    }

    fun loginWithOAuth(view: View) {
        val configuration = OAuthConfiguration(OAuthProvider.GOOGLE, "link.magic.demo://callback")
        magic.oauth.loginWithPopup(this, configuration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = magic.oauth.getResult(data)

        result.whenComplete { response: OAuthResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && !response.hasError()) {
                response.result.magic.idToken?.let { Log.d("login", it) }
                intent = Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("login", "Not Logged in")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun loginWithEmail(v: View) {
        val configuration = LoginWithMagicLinkConfiguration(et.text.toString())
        val result = (magic as Magic).auth.loginWithMagicLink(this, configuration)
        toastAsync("Logging in...")
        result.whenComplete { token: DIDToken?, error: Throwable? ->
            if (error != null) {
                //Log.d("TAG", "loginWithEmailtoken: "+token)
                Log.d("error", error.localizedMessage)
            }
            if (token != null && !token.hasError()) {
                //Log.d("loginwith", token.result)
                Log.d("TAG", "loginWithEmailtoken: "+token.result)
             /*   intent = Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)*/
                val intent = Intent(this, HomeActivity::class.java)
                // start your next activity
                startActivity(intent)
            } else {
                Log.d("login", "Unable to login")
            }
        }
    }


    private fun mcLogin(v: View) {
        val accounts = (magic as Magic).wallet.connectWithUI(this)
        accounts.whenComplete { response: ConnectWithUIResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && !response.hasError()) {
                response.result?.let { Log.d("Your Public Address is:", it.first()) }
                val intent = Intent(this, HomeActivity::class.java)
                // start your next activity
                startActivity(intent)
            } else {
                response?.result?.let { Log.d("Your Public Address is:", it.first()) }
                Log.i("mcLogin RESPONSE", "Response is: ${response.toString()}")
                Log.d("MC Login", "Magic Connect Not logged in")
            }
        }
    }

    fun toastAsync(message: String?) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
    }
}