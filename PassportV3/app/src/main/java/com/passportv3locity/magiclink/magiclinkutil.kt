package com.passportv3locity.magiclink

import android.app.Application
import com.passportv3locity.common.KeyAndLink
import link.magic.android.Magic

class magiclinkutil : Application() {
    lateinit var magic: Magic

    override fun onCreate() {
        magic = Magic(this, KeyAndLink.MAGIC_LINK_API_KEY)
        super.onCreate()
    }
}