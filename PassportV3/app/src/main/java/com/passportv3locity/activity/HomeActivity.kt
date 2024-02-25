package com.passportv3locity.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.passportv3locity.R
import com.passportv3locity.applocal.MyApplication.Companion.magic
import com.passportv3locity.databinding.ActivityHomeBinding
import com.passportv3locity.fragments.PassesFragment
import com.passportv3locity.fragments.PassportFragment
import com.passportv3locity.fragments.SettingFragment
import com.passportv3locity.fragments.VisasFragment
import link.magic.android.modules.user.response.LogoutResponse

class HomeActivity : AppCompatActivity() {
    lateinit var homeBinding: ActivityHomeBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        supportActionBar?.hide()
        this.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
        loadFragment(PassesFragment())

        //Perform to change view with the click on bottom navigation item
        homeBinding.bottomNavigationBar.setOnNavigationItemSelectedListener { menu ->
            val selectedItemId: Int =
                homeBinding.bottomNavigationBar.getSelectedItemId()

            when (menu.itemId) {
                R.id.setting -> {
                    if (R.id.setting != selectedItemId)
                        loadFragment(SettingFragment())
                    true
                }
                R.id.pass -> {
                    if (R.id.pass != selectedItemId)
                        loadFragment(PassesFragment())
                    true
                }
                R.id.passport -> {
                    if (R.id.passport != selectedItemId)
                        loadFragment(PassportFragment())
                    true
                }
                R.id.visa -> {
                    if (R.id.visa != selectedItemId)
                        loadFragment(VisasFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}