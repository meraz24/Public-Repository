package com.passportv3locity.applocal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.calvarytemple.notificationmanager.NotificationCallbackListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.messaging.FirebaseMessaging
import com.passportv3locity.common.KeyAndLink
import link.magic.android.Magic
import kotlin.collections.ArrayList

class MyApplication : Application(), Application.ActivityLifecycleCallbacks  {
    var mContext: Context? = null


    companion object {
        private val TAG = MyApplication::class.java.simpleName
        private var mInstance: MyApplication? = null
        var mLocalStore: AppLocalStore? = null
        lateinit var magic: Magic
        var notificationCallbackListener: NotificationCallbackListener? = null
        var list_activities: ArrayList<Activity>? = ArrayList()
        val app: MyApplication?
            get() {
                if (mInstance == null) {
                    mInstance = MyApplication()
                }
                return mInstance
            }

        @get:Synchronized
        val instance: MyApplication?
            get() {
                if (mInstance == null) {
                    mInstance = MyApplication()
                }
                return mInstance
            }

        val currentActivity: Activity?
            get() = if (list_activities == null || list_activities!!.size == 0) {
                null
            } else list_activities!![list_activities!!.size - 1]

    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        mContext = applicationContext
        if (mLocalStore == null) {
            mLocalStore = AppLocalStore.getInstance(this)
        }
        magic = Magic(this, KeyAndLink.MAGIC_LINK_API_KEY)
        registerActivityLifecycleCallbacks(this)
        mInstance = this
       // generateDeviceTokenWithCheck()

        //enableSocketLogs()
    }
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        list_activities!!.add(activity)
        Log.v("ActivityList", list_activities!!.size.toString() + "")
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityResumed(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        list_activities!!.remove(activity)
        Log.v("ActivityList", list_activities!!.size.toString() + "")
    }

    fun generateDeviceTokenWithCheck() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isComplete) {
                generateDeviceTokenWithCheck()
                return@addOnCompleteListener
            }
            // Get new Instance ID token
            var token = "00000"
            try {
                token = it.result.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }

           /* if(!mLocalStore?.firebaseToken.equals(token)) {
                Log.d("FIREBASE_TOKEN", "" + token)
                mLocalStore?.saveFirebaseToken(token)
                instance?.getNewFCMTokenListener()?.onNotificationReceived()
            }*/
        }
    }

    fun getNotificationCallbackListener(): NotificationCallbackListener? {
        return notificationCallbackListener
    }

    fun setNotificationCallbackListener(notificationCallbackLis: NotificationCallbackListener) {
        notificationCallbackListener = notificationCallbackLis
    }

    fun getNewFCMTokenListener(): NotificationCallbackListener? {
        return notificationCallbackListener
    }

    fun setNewFCMTokenListener(notificationCallbackLis: NotificationCallbackListener) {
        notificationCallbackListener = notificationCallbackLis
    }


}