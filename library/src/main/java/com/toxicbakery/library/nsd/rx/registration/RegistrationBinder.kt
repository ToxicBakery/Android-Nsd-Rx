package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.INsdManagerCompat

data class RegistrationBinder(
        private val nsdManagerCompat: INsdManagerCompat,
        private val listener: NsdManager.RegistrationListener
) {

    fun unregister() = nsdManagerCompat.unregisterService(listener)

}