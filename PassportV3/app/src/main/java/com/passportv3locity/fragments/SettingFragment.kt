package com.passportv3locity.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.passportv3locity.R
import com.passportv3locity.activity.MainActivity
import com.passportv3locity.applocal.MyApplication
import com.passportv3locity.databinding.FragmentSettingBinding
import com.passportv3locity.utils.setSafeOnClickListener
import link.magic.android.modules.user.response.LogoutResponse

class SettingFragment : Fragment() {

    private var settingBinding:FragmentSettingBinding? = null
    private val binding get() = settingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        settingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBinding?.logout?.setSafeOnClickListener {
            logout(it)
        }
    }

    fun logout(v: View) {
        //tabActivity.toastAsync("Logging out...")
        val completable = MyApplication.magic.user.logout(requireContext())
        completable.whenComplete { response: LogoutResponse?, error: Throwable? ->
            if (error != null) {
                Log.d("error", error.localizedMessage)
            }
            if (response != null && response.result) {
                //tabActivity.toastAsync("You're logged out!")
                var intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}