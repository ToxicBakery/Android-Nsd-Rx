package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.INsdManagerCompat
import com.toxicbakery.library.nsd.rx.mock
import com.toxicbakery.library.nsd.rx.verify
import org.junit.Test

class RegistrationBinderTest {

    @Test
    fun unregister() {
        val nsdManagerCompat: INsdManagerCompat = mock()
        val listener: NsdManager.RegistrationListener = mock()
        RegistrationBinder(nsdManagerCompat, listener).unregister()
        verify(nsdManagerCompat).unregisterService(listener)
    }

}