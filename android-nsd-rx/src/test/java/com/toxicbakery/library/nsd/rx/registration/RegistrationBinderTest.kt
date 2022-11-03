package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.INsdManagerCompat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RegistrationBinderTest {

    @Test
    fun unregister() {
        val nsdManagerCompat: INsdManagerCompat = mock()
        val listener: NsdManager.RegistrationListener = mock()
        RegistrationBinder(nsdManagerCompat, listener).unregister()
        verify(nsdManagerCompat).unregisterService(listener)
    }

}
