package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryBinder
import org.junit.Test

class DiscoveryBinderTest {

    @Test
    fun stop() {
        val nsdManagerCompat: INsdManagerCompat = mock()
        val listener: NsdManager.DiscoveryListener = mock()
        DiscoveryBinder(nsdManagerCompat, listener).stop()
        verify(nsdManagerCompat).stopServiceDiscovery(listener)
    }

}